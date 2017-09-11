package www.breadboy.com.lockerroom.data.source.local

import io.reactivex.functions.Function
import io.realm.Realm
import io.realm.RealmResults
import www.breadboy.com.lockerroom.data.App
import www.breadboy.com.lockerroom.data.source.AppsDataSource

/**
 * Created by N4039 on 2017-09-07.
 */

class AppsLocalDataSource : AppsDataSource {

    val lockedAppMemoryCache: MutableList<App> = mutableListOf()

    override fun loadApp(appPackageName: String): App? = loadAppFromList(appPackageName) ?: loadAppFromRealm(appPackageName)

    private fun loadAppFromList(appPackageName: String): App? = lockedAppMemoryCache.firstOrNull { appPackageName.equals(it.appPackageName, true) }

    private fun loadAppFromRealm(appPackageName: String): App? {
        var app: App? = null

        RealmFlowable()
                .getRealmObject(Function<Realm, RealmApp> { realm -> realm.where(RealmApp::class.java).equalTo("package_name", appPackageName).findFirst()!! })
                .map { realmApp -> app = realmApp.let { App(it.appPackageName!!, it.appIconId, it.appName!!, it.isLocked) } }
                .subscribe()

        return app
    }

    override fun loadApps() = RealmFlowable()
            .getRealmResult(Function<Realm, RealmResults<RealmApp>> { realm -> realm.where(RealmApp::class.java).findAll() })
            .map(object : Function<RealmResults<RealmApp>, List<App>> {
                override fun apply(realmResults: RealmResults<RealmApp>): List<App> {
                    mutableListOf<App>().let {
                        realmResults.forEach { realmApp ->  it.add(App(realmApp.appPackageName!!, realmApp.appIconId, realmApp.appName!!, realmApp.isLocked)) }

                        return it
                    }
                }
            })

    override fun saveApp(app: App) {
        saveAppToList(app)
        saveAppToRealm(app)
    }

    private fun saveAppToList(app: App) =
            lockedAppMemoryCache.firstOrNull { app.appPackageName.equals(it.appPackageName, true) } ?: lockedAppMemoryCache.add(app)

    private fun saveAppToRealm(app: App) = RealmFlowable()
            .getRealmObject(Function<Realm, RealmApp> { realm -> realm.copyToRealmOrUpdate(appToRealm(app)) })
            .subscribe()


    override fun saveApps(appList: List<App>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteApp(app: App) {
        deleteAppToList(app)
        deleteAppToRealm(app)
    }

    private fun deleteAppToList(app: App) = lockedAppMemoryCache.remove(app)

    private fun deleteAppToRealm(app: App) = RealmFlowable()
            .getRealmResult(Function<Realm, RealmResults<RealmApp>> { realm -> realm.where(RealmApp::class.java).equalTo("package_name", app.appPackageName).findAll() })
            .map { realmResults -> realmResults.deleteAllFromRealm() }
            .subscribe()

    override fun deleteApps() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun appToRealm(app: App): RealmApp =
            RealmApp().apply {
                appPackageName = app.appPackageName
                appIconId = app.appIconId
                appName = app.appName
                isLocked = app.isLocked
            }

    override fun isLockedByPackageName(appPackageName: String) = RealmFlowable()
            .getRealmObject(Function<Realm, RealmApp> {
                realm -> realm.where(RealmApp::class.java).equalTo("package_name", appPackageName).findFirst() as RealmApp })
            //.map { realmApp -> loadApp(realmApp) }
}