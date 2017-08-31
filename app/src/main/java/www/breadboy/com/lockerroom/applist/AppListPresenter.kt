package www.breadboy.com.lockerroom.applist

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
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

    fun getInstalledAppsByParts() =
            Flowable.create({ emitter: FlowableEmitter<App> ->
                        val applicationInfoList = appListActivity.packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES)

                        applicationInfoList
                                .filter { !it.packageName.isNullOrBlank() && it.icon != 0 }
                                .forEach { emitter.onNext(App(it.packageName, it.icon, appListActivity.packageManager.getApplicationLabel(it).toString())) }
                    }, BackpressureStrategy.BUFFER)

}