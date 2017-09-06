package www.breadboy.com.lockerroom.applist

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_app_list.*
import www.breadboy.com.lockerroom.base.BasePresenter
import www.breadboy.com.lockerroom.data.App
import javax.inject.Inject


/**
 * Created by N4039 on 2017-08-28.
 */

class AppListPresenter
@Inject
constructor(val appListActivity: AppListActivity) : BasePresenter {

    val applicationInfoList: List<ApplicationInfo> = appListActivity.packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES)
    var appListStartIdx = 0

    companion object {
        const val MAX_LOADING_APP_LENGTH = 25
        const val ADJUSTED_VALUE = 5
    }

    override fun start() {
        getInstalledAppsByParts(appListStartIdx)
    }

    fun getInstalledAppsByParts(startIndex: Int) = getInstalledAppsDispoable(startIndex)

    private fun getInstalledAppsFlowable(startIndex: Int) =
            Flowable.fromIterable(applicationInfoList)
                    .filter { !it.packageName.isNullOrEmpty() && it.icon != 0 }
                    .filter { applicationInfoList.indexOf(it) in startIndex until startIndex + MAX_LOADING_APP_LENGTH }
                    .map { appInfo -> App(appInfo.packageName, appInfo.icon, appListActivity.packageManager.getApplicationLabel(appInfo).toString()) }
                    .doOnSubscribe { appListStartIdx += MAX_LOADING_APP_LENGTH }

    private fun getInstalledAppsDispoable(startIndex: Int) =
            getInstalledAppsFlowable(startIndex)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onBackpressureBuffer()
                    .subscribe(
                            { app ->
                                appListActivity.progressbar_app_list_activity.visibility = View.VISIBLE
                                appListActivity.appListAdapter.addApp(app)
                            },
                            {
                                throwable ->
                                Log.e("$javaClass (getInstalledAppsByParts)", "${throwable.printStackTrace()}")
                                appListActivity.progressbar_app_list_activity.visibility = View.GONE
                            },
                            {
                                appListActivity.progressbar_app_list_activity.visibility = View.GONE
                            },
                            {
                                subscription -> subscription.request(Long.MAX_VALUE)
                            })
}