package www.breadboy.com.lockerroom.applist

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import io.reactivex.Flowable
import www.breadboy.com.lockerroom.base.BasePresenter
import www.breadboy.com.lockerroom.data.App
import javax.inject.Inject


/**
 * Created by N4039 on 2017-08-28.
 */

class AppListPresenter

@Inject
constructor(val appListActivity: AppListActivity) : BasePresenter {

    val applicationInfoList = appListActivity.packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES)
    var appListStartIdx = 0

    companion object {
        const val MAX_LOADING_APP_LEN = 10
    }



    override fun start() {
        getInstalledAppsByParts(appListStartIdx).subscribe({ app -> appListActivity.appListAdapter.addApp(app) })
    }

    fun getInstalledAppsByParts(startIndex: Int): Flowable<App> =
            Flowable.fromIterable(applicationInfoList)
                    .filter { !it.packageName.isNullOrEmpty() && it.icon != 0 }
                    .filter { applicationInfoList.indexOf(it) in startIndex until startIndex + MAX_LOADING_APP_LEN }
                    .map({ appInfo: ApplicationInfo ->
                        App(appInfo.packageName, appInfo.icon, appListActivity.packageManager.getApplicationLabel(appInfo).toString()) })
    
}