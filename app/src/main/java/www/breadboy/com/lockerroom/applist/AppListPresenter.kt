package www.breadboy.com.lockerroom.applist

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.functions.Function
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
        getInstalledAppsByParts().subscribe { app -> appListActivity.appListAdapter.addApps(app) }
    }

    fun getInstalledAppsByParts(applicationInfoList: MutableList<ApplicationInfo>, startIndex: Int) =
            Flowable.range(startIndex, startIndex + 14)
                    .map(Function1<List<ApplicationInfo>, List<App>>)

    /*Function<List<ApplicationInfo>, List<App>> {
        val appMutableList = arrayListOf<App>()

        for (appInfo in applicationInfoList) {
            appMutableList.add(App(appInfo.packageName, appInfo.icon, appListActivity.packageManager.getApplicationLabel(appInfo).toString()))
        }

        return appMutableList*/

    /*create({ emitter: FlowableEmitter<App> ->
                        val applicationInfoList = appListActivity.packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES)

                        applicationInfoList
                                .filter { !it.packageName.isNullOrBlank() && it.icon != 0 }
                                .forEach { emitter.onNext(App(it.packageName, it.icon, appListActivity.packageManager.getApplicationLabel(it).toString())) }
                    }, BackpressureStrategy.BUFFER)*/

}