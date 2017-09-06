package www.breadboy.com.lockerroom.applist

import android.content.pm.ApplicationInfo
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import www.breadboy.com.lockerroom.data.App
import javax.inject.Inject


/**
 * Created by N4039 on 2017-08-28.
 */

class AppListPresenter

@Inject
constructor(val appListActivity: AppListActivity,
            val installedAppList: MutableList<ApplicationInfo>) : AppListContract.Presenter {

    var appListStartIdx: Int
        get() = appListActivity.appListStartIdx
        set(value) {
            appListActivity.appListStartIdx = value
        }

    companion object {
        const val MAX_LOADING_APP_LENGTH = 25
    }

    override fun start() {
        getInstalledAppsByParts(appListStartIdx)
    }

    override fun getInstalledAppsByParts(startIndex: Int) = getInstalledAppsDispoable(startIndex)

    override fun getInstalledAppsFlowable(startIndex: Int) =
            Flowable.fromIterable(installedAppList)
                    .filter { !it.packageName.isNullOrEmpty() && it.icon != 0 }
                    .filter { installedAppList.indexOf(it) in startIndex until startIndex + MAX_LOADING_APP_LENGTH }
                    .map { appInfo -> App(appInfo.packageName, appInfo.icon, appListActivity.packageManager.getApplicationLabel(appInfo).toString()) }
                    .doOnSubscribe { appListStartIdx += MAX_LOADING_APP_LENGTH }

    override fun getInstalledAppsDispoable(startIndex: Int) =
            getInstalledAppsFlowable(startIndex)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onBackpressureBuffer()
                    .subscribe(
                            { app ->
                                appListActivity.showProgressBar()
                                appListActivity.appListAdapter.addApp(app)
                            },
                            {
                                throwable -> Log.e("$javaClass (getInstalledAppsByParts)", "${throwable.printStackTrace()}")
                                appListActivity.hideProgressBar()
                            },
                            {
                                appListActivity.hideProgressBar()
                            },
                            {
                                subscription -> subscription.request(Long.MAX_VALUE)
                            })
}