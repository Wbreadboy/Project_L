package www.breadboy.com.lockerroom.data

import android.content.Context
import io.reactivex.Flowable
import www.breadboy.com.lockerroom.data.local.realm.RealmApp

/**
 * Created by N4039 on 2017-09-07.
 */
interface AppsDataSource {

    fun loadApp(appPackageName: String): App?
    fun loadApps(context: Context): Flowable<*>

    fun saveApp(app: App)
    fun saveApps(appList: List<App>)

    fun deleteApp(app: App)
    fun deleteApps()

    fun appToRealm(app: App): RealmApp

    fun isLockedByPackageName(appPackageName: String): Flowable<*>
}