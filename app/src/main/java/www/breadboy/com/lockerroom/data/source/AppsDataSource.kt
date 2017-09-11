package www.breadboy.com.lockerroom.data.source

import io.reactivex.Flowable
import www.breadboy.com.lockerroom.data.App
import www.breadboy.com.lockerroom.data.source.local.RealmApp

/**
 * Created by N4039 on 2017-09-07.
 */
interface AppsDataSource {

    fun loadApp(appPackageName: String): App?
    fun loadApps(): Flowable<*>

    fun saveApp(app: App)
    fun saveApps(appList: List<App>)

    fun deleteApp(app: App)
    fun deleteApps()

    fun appToRealm(app: App): RealmApp

    fun isLockedByPackageName(appPackageName: String): Flowable<*>
}