package www.breadboy.com.lockerroom.applist

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

    }

    fun getInstalledAppsByParts() = Flowable.create({ emitter: FlowableEmitter<App> ->
        val applicationInfoList = appListActivity.packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES)

        for (applicationInfo in applicationInfoList) {
            if (applicationInfo.packageName != null && applicationInfo.icon != 0) {
                emitter.onNext(App(applicationInfo.packageName, applicationInfo.icon))
            }
        }
    }, BackpressureStrategy.BUFFER)
}