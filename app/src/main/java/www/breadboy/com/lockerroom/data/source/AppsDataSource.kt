package www.breadboy.com.lockerroom.data.source

import io.reactivex.Flowable
import www.breadboy.com.lockerroom.data.App
import www.breadboy.com.lockerroom.data.source.local.RealmApp

/**
 * Created by N4039 on 2017-09-07.
 */
interface AppsDataSource {

    fun loadAppByList(appPackageName: String): App?
    fun loadAppsByList(): Flowable<*>

    fun loadAppByRealm()
    fun loadAppsByRealm()

    fun saveAppToList(app: App)
    fun saveAppsToList(appList: List<App>)

    fun deleteApp()
    fun deleteApps()

    fun isLockedByPackageName(appPackageName: String): Flowable<*>
}