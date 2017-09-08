package www.breadboy.com.lockerroom.data.source.local

import io.reactivex.functions.Action
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

    override fun loadAppByList(appPackageName: String): App? = lockedAppMemoryCache.firstOrNull { appPackageName.equals(it.appPackageName, true) }

    override fun loadAppsToList() = RealmFlowable()
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
        val realmApp = RealmApp().apply {
            appPackageName = app.appPackageName
            appIconId = app.appIconId
            appName = app.appName
            isLocked = app.isLocked
        }

        RealmFlowable()
                .getRealmObject(Function<Realm, RealmApp> { realm -> realm.copyToRealmOrUpdate(realmApp) })
                //.map({ realmApp -> loadApp(realmApp) })
    }

    override fun saveApps(appList: List<App>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteApp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteApps() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isLockedByPackageName(appPackageName: String) = RealmFlowable()
            .getRealmObject(Function<Realm, RealmApp> {
                realm -> realm.where(RealmApp::class.java).equalTo("package_name", appPackageName).findFirst() as RealmApp })
            //.map { realmApp -> loadApp(realmApp) }
}