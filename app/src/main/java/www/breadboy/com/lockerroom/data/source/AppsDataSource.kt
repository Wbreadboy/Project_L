package www.breadboy.com.lockerroom.data.source

/**
 * Created by N4039 on 2017-09-07.
 */
interface AppsDataSource {

    fun loadApp()

    fun loadApps()

    fun saveApp()

    fun saveApps()

    fun deleteApp()

    fun deleteApps()
}