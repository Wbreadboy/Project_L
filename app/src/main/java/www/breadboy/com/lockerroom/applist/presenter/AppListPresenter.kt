package www.breadboy.com.lockerroom.applist.presenter

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import www.breadboy.com.lockerroom.applist.AppListContract
import www.breadboy.com.lockerroom.applist.view.AppListActivity
import www.breadboy.com.lockerroom.applist.view.AppListViewHolder
import www.breadboy.com.lockerroom.data.App
import www.breadboy.com.lockerroom.data.local.AppsLocalDataSource
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
    }

    var installedAppInfoList: MutableList<ApplicationInfo> = mutableListOf()
    fun installedAppInfomations() =
        Flowable.create<MutableList<ApplicationInfo>>({
            if (installedAppInfoList.isEmpty()) {
                installedAppInfoList = appListActivity.packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES)
                installedAppInfoList
                        .filter { it.icon == 0 }
                        .map { installedAppInfoList.remove(it) }
            }

            it.onNext(installedAppInfoList)
            it.onComplete()
        }, BackpressureStrategy.BUFFER)
                //.sorted()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    Flowable.fromIterable(installedAppInfoList)
                            .filter { installedAppInfoList.indexOf(it) in appListStartIdx until appListStartIdx + MAX_LOADING_APP_LENGTH }

                            /*.buffer(Flowable.fromIterable(installedAppInfoList)
                                    .filter { (installedAppInfoList.indexOf(it) >= appListStartIdx + MAX_LOADING_APP_LENGTH - 1) or
                                            (installedAppInfoList.indexOf(it) >= installedAppInfoList.lastIndex) }
                                    .map { App(it.packageName, it.icon, appListActivity.packageManager.getApplicationLabel(it).toString(),
                                            appsLocalDataSource.loadApp(it.packageName)?.locked ?: false) }
                                    .take(1))*/
                            .window(25)

                            .map { appInfo -> App(
                                    appInfo.packageName,
                                    appInfo.icon,
                                    appListActivity.packageManager.getApplicationLabel(appInfo).toString(),
                                    appsLocalDataSource.loadApp(appInfo.packageName)?.locked ?: false)
                            }

                            //.filter { installedAppInfoList.indexOf(it) in appListStartIdx until appListStartIdx + MAX_LOADING_APP_LENGTH }
                            .doOnSubscribe {
                                appListStartIdx += MAX_LOADING_APP_LENGTH
                                Log.e("!!!!!!!!!!!!!!!!!", "$appListStartIdx")
                            }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .onBackpressureBuffer()
                            .subscribe(
                                    { app ->
                                        Log.e("!!!!!!!!!!!!!!!", "${app.size}")
                                        appListActivity.showProgressBar()
                                        appListActivity.appListAdapter.addApps(app)
                                    },
                                    {
                                        throwable -> Log.e("$javaClass (getInstalledAppsByParts)", "${throwable.printStackTrace()}")
                                        appListActivity.hideProgressBar()
                                    },
                                    {
                                        appListActivity.appListAdapter.notifyDataSetChanged()
                                        appListActivity.hideProgressBar()
                                    },
                                    {
                                        subscription -> subscription.request(Long.MAX_VALUE)
                                    })
                })

    override fun getInstalledAppsByParts(startIndex: Int) = getInstalledAppsDispoable(startIndex)

    override fun getInstalledAppsFlowable(startIndex: Int) =
            Flowable.fromIterable(installedAppList)
                    .filter { !it.packageName.isNullOrEmpty() && it.icon != 0 }
                    .filter { installedAppList.indexOf(it) in startIndex until startIndex + MAX_LOADING_APP_LENGTH }
                    .map { appInfo -> App(
                            appInfo.packageName,
                            appInfo.icon,
                            appListActivity.packageManager.getApplicationLabel(appInfo).toString(),
                            appsLocalDataSource.loadApp(appInfo.packageName)?.locked ?: false)
                    }
                    .doOnSubscribe {
                        appListStartIdx += MAX_LOADING_APP_LENGTH
                    }


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
                                appListActivity.appListAdapter.notifyDataSetChanged()
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

                    //getInstalledAppsByParts(appListStartIdx)
                    installedAppInfomations()
                }
                }, {
                    throwable -> Log.e("$javaClass (requestAppMultiple)", "${throwable.printStackTrace()}")
                }, {

                }, {
                    subscription -> subscription.request(Long.MAX_VALUE)
                })
}