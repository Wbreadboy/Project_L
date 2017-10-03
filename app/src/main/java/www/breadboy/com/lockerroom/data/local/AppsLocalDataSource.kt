package www.breadboy.com.lockerroom.data.local

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.Function
import io.realm.Realm
import io.realm.RealmResults
import www.breadboy.com.lockerroom.data.App
import www.breadboy.com.lockerroom.data.AppsDataSource
import www.breadboy.com.lockerroom.data.local.realm.RealmApp
import www.breadboy.com.lockerroom.data.local.rx.RealmFlowable

/**
 * Created by N4039 on 2017-09-07.
 */

class AppsLocalDataSource : AppsDataSource {

    private var mutableInstalledAppInfoCache = mutableListOf<ApplicationInfo>()
    private val mutableLockedAppCache = mutableListOf<App>()

    override fun loadApp(appPackageName: String): App? = loadAppFromList(appPackageName)

    private fun loadAppFromList(appPackageName: String): App? = mutableLockedAppCache.firstOrNull { appPackageName.equals(it.packageName, true) }

    private fun loadAppFromRealm(appPackageName: String): App? {
        var app: App? = null

        RealmFlowable()
                .getRealmObject(Function<Realm, RealmApp> { realm -> realm.where(RealmApp::class.java).equalTo("packageName", appPackageName).findFirst() ?: RealmApp() })
                .map { realmApp -> app = realmApp.let { App(it.packageName, it.iconId, it.appName, it.locked) } }
                .subscribe()

        return app
    }

    private fun loadInstalledAppsInfo(context: Context) =
            Flowable.create<MutableList<ApplicationInfo>>({
                mutableInstalledAppInfoCache = context.packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES)
                mutableInstalledAppInfoCache
                        .filter { it.icon == 0 }
                        .forEach { mutableInstalledAppInfoCache.remove(it) }

                it.onNext(mutableInstalledAppInfoCache)
                it.onComplete()
            }, BackpressureStrategy.BUFFER)

    private fun loadLockedAppsFromRealm() = RealmFlowable()
            .getRealmResult(Function<Realm, RealmResults<RealmApp>> { realm -> realm.where(RealmApp::class.java).findAll() })
            .map { realmResults ->
                mutableLockedAppCache.let {
                    realmResults.forEach {
                        realmApp -> it.add(App(realmApp.packageName, realmApp.iconId, realmApp.appName, realmApp.locked))
                    }
                }
            }

    private fun loadLockedAppFromCache(appPackageName: String): App? = mutableLockedAppCache.firstOrNull { appPackageName.equals(it.packageName, true) }

    override fun loadApps(context: Context): Flowable<App> =
            if (mutableLockedAppCache.isEmpty() && mutableInstalledAppInfoCache.isEmpty()) {
                loadAppsFromLocal(context)
            } else {
                loadAppsFromCache(context)
            }

    private fun loadAppsFromLocal(context: Context): Flowable<App> =
            Flowable.concat(loadLockedAppsFromRealm(), loadInstalledAppsInfo(context))
                    .skip(1)
                    .flatMap { t ->
                        t as List<*>

                        Flowable.fromIterable(t)
                    }
                    .map {
                        it as ApplicationInfo

                        App(it.packageName,
                                it.icon,
                                context.packageManager.getApplicationLabel(it).toString(),
                                loadLockedAppFromCache(it.packageName)?.locked ?: false) }

    private fun loadAppsFromCache(context: Context): Flowable<App> =
            Flowable.fromIterable(mutableInstalledAppInfoCache)
                    .map { App(it.packageName,
                            it.icon,
                            context.packageManager.getApplicationLabel(it).toString(),
                            loadLockedAppFromCache(it.packageName)?.locked ?: false) }

    override fun saveApp(app: App) {
        saveAppToList(app)
        saveAppToRealm(app)
    }

    private fun saveAppToList(app: App) =
            mutableLockedAppCache.firstOrNull { app.packageName.equals(it.packageName, true) } ?: mutableLockedAppCache.add(app)

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

    private fun deleteAppToList(app: App) = mutableLockedAppCache.remove(app)

    private fun deleteAppToRealm(app: App) = RealmFlowable()
            .getRealmResult(Function<Realm, RealmResults<RealmApp>> { realm -> realm.where(RealmApp::class.java).equalTo("packageName", app.packageName).findAll() })
            .map { realmResults -> realmResults.deleteAllFromRealm() }
            .subscribe()

    override fun deleteApps() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun appToRealm(app: App): RealmApp =
            RealmApp().apply {
                packageName = app.packageName
                iconId = app.iconId
                appName = app.appName
                locked = app.locked
            }

    override fun isLockedByPackageName(appPackageName: String) = RealmFlowable()
            .getRealmObject(Function<Realm, RealmApp> {
                realm -> realm.where(RealmApp::class.java).equalTo("packageName", appPackageName).findFirst() as RealmApp
            })
            //.map { realmApp -> loadApp(realmApp) }
}