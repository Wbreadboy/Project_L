package www.breadboy.com.lockerroom.applist.presenter

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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
            val appsLocalDataSource: AppsLocalDataSource) : AppListContract.Presenter {

    companion object {
        const val MAX_LOADING_APP_LENGTH = 35
    }

    override fun start() {
        loadInstalledApps(0)
    }

    override fun loadInstalledApps(page: Long): Disposable = appsLocalDataSource
            .loadApps(appListActivity)
            .buffer(MAX_LOADING_APP_LENGTH)
            .elementAt(page)
            .doOnSubscribe { appListActivity.isLoading = true }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { apps ->
                        appListActivity.apply {
                            addAppsToRecyclerView(apps)
                            hideProgressBar()
                            isLoading = false
                        }
                    },
                    { throwable ->
                        Log.e("$javaClass (getInstalledAppsByParts)", "${throwable.printStackTrace()}")

                        appListActivity.apply {
                            hideProgressBar()
                            isLoading = false
                        }
                    },
                    {
                        appListActivity.apply {
                            hideProgressBar()
                            isLoading = false
                        }
                    })

    override fun onInstalledAppClick(holder: AppListViewHolder?, position: Int, app: App) =
            Single.just(app)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { appInfo ->
                                appListActivity.appListAdapter.let {
                                    if (appInfo.locked) {
                                        it.wrapUnlockedModeAtLayout(holder, position)
                                        appsLocalDataSource.deleteApp(app)
                                    } else {
                                        it.wrapLockedModeAtLayout(holder, position)
                                        appsLocalDataSource.saveApp(app)
                                    }
                                }
                            },
                            { throwable ->
                                Log.e("$javaClass (onInstalledAppClick)", "${throwable.printStackTrace()}")
                            }
                    )
}