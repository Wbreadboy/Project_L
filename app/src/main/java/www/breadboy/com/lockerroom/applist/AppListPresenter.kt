package www.breadboy.com.lockerroom.applist

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import www.breadboy.com.lockerroom.base.BasePresenter
import www.breadboy.com.lockerroom.data.App
import javax.inject.Inject

/**
 * Created by N4039 on 2017-08-28.
 */
class AppListPresenter

@Inject
constructor(val appListActivity: AppListActivity) : BasePresenter {

    override fun start() {
        val appApplicationInfoList: MutableList<ApplicationInfo> =
                //appListActivity.packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER), 0)
                appListActivity.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (applicatoinInfo in appApplicationInfoList) {
            appListActivity.appListAdapter.addApp(App(applicatoinInfo.packageName))
        }
    }
}