package www.breadboy.com.lockerroom.applist.presenter

import android.content.pm.ApplicationInfo
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import www.breadboy.com.lockerroom.applist.AppListContract
import www.breadboy.com.lockerroom.applist.view.AppListActivity
import www.breadboy.com.lockerroom.applist.view.AppListViewHolder
import www.breadboy.com.lockerroom.data.App
import www.breadboy.com.lockerroom.data.source.local.AppsLocalDataSource
import javax.inject.Inject


/**
 * Created by N4039 on 2017-08-28.
 */

class AppListPresenter

@Inject
constructor(val appListActivity: AppListActivity,
            val installedAppList: MutableList<ApplicationInfo>,
            val appsLocalDataSource: AppsLocalDataSource) : AppListContract.Presenter {

    var appListStartIdx: Int
        get() = appListActivity.appListStartIdx
        set(value) {
            appListActivity.appListStartIdx = value
        }

    companion object {
        const val MAX_LOADING_APP_LENGTH = 25
    }

    override fun start() {
        requestAppMultipleFromRealm()
        getInstalledAppsByParts(appListStartIdx)
    }

    override fun getInstalledAppsByParts(startIndex: Int) = getInstalledAppsDispoable(startIndex)

    override fun getInstalledAppsFlowable(startIndex: Int) =
            Flowable.fromIterable(installedAppList)
                    .filter { !it.packageName.isNullOrEmpty() && it.icon != 0 }
                    .filter { installedAppList.indexOf(it) in startIndex until startIndex + MAX_LOADING_APP_LENGTH }
                    .map { appInfo -> App(
                            appInfo.packageName,
                            appInfo.icon,
                            appListActivity.packageManager.getApplicationLabel(appInfo).toString(),
                            false)
                    }
                    .doOnSubscribe { appListStartIdx += MAX_LOADING_APP_LENGTH }

    override fun getInstalledAppsDispoable(startIndex: Int) =
            getInstalledAppsFlowable(startIndex)
                    .subscribeOn(Schedulers.io())
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

    override fun onInstalledAppClick(holder: AppListViewHolder?, position: Int, app: App) =
        Single.just(app)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            appInfo -> appListActivity.appListAdapter.let {
                                if (appInfo.locked) {
                                    it.wrapUnlockedModeAtLayout(holder, position)
                                    appsLocalDataSource.deleteApp(app)
                                } else {
                                    it.wrapLockedModeAtLayout(holder, position)
                                    appsLocalDataSource.saveApp(app)
                                }
                            }
                        },
                        {
                            throwable -> Log.e("$javaClass (onInstalledAppClick)", "${throwable.printStackTrace()}")
                        }
                )

    fun requestAppMultipleFromRealm() = appsLocalDataSource
                .loadApps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    list -> run {
                    appsLocalDataSource.lockedAppMemoryCache.apply {
                        clear()
                        addAll(list)
                    }
                }
                }, {
                    throwable -> Log.e("$javaClass (requestAppMultiple)", "${throwable.printStackTrace()}")
                }, {

                }, {
                    subscription -> subscription.request(Long.MAX_VALUE)
                })
}