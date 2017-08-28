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
                appListActivity.packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES)

        for (applicatoinInfo in appApplicationInfoList) {
            if (applicatoinInfo.packageName != null && applicatoinInfo.icon != 0) {
                appListActivity.appListAdapter.addApp(App(applicatoinInfo.packageName, applicatoinInfo.icon))
            }
        }
    }
}