package www.breadboy.com.lockerroom.applist

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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

    fun getInstalledAppsByParts(startIndex: Int): Disposable =
            Flowable.fromIterable(applicationInfoList)
                    .filter { !it.packageName.isNullOrEmpty() && it.icon != 0 }
                    .filter { applicationInfoList.indexOf(it) in startIndex until startIndex + MAX_LOADING_APP_LENGTH }
                    .map { appInfo -> App(appInfo.packageName, appInfo.icon, appListActivity.packageManager.getApplicationLabel(appInfo).toString()) }

                    .doOnSubscribe {
                        appListStartIdx += MAX_LOADING_APP_LENGTH
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())

                    .onBackpressureBuffer()
                    .subscribe({
                        app -> appListActivity.appListAdapter.addApp(app)
                    }, {
                        throwable -> Log.e("$javaClass (getInstalledAppsByParts)", "$throwable")
                    })

}