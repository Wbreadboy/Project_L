package www.breadboy.com.lockerroom.applist.di

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.support.v7.widget.StaggeredGridLayoutManager
import dagger.Module
import dagger.Provides
import www.breadboy.com.lockerroom.applist.ActivityScope
import www.breadboy.com.lockerroom.applist.view.AppListActivity
import www.breadboy.com.lockerroom.applist.view.AppListAdapter
import www.breadboy.com.lockerroom.applist.presenter.AppListPresenter
import www.breadboy.com.lockerroom.base.BaseModule

/**
 * Created by SDG on 2017. 8. 22..
 */

@Module
class AppListModule(var appListActivity: AppListActivity) : BaseModule<AppListActivity>(appListActivity) {

    @ActivityScope
    @Provides
    fun provideAppListActivity() = appListActivity

    @ActivityScope
    @Provides
    fun provideAppListPresenter(appListActivity: AppListActivity,
                                installedAppList: MutableList<ApplicationInfo>) = AppListPresenter(appListActivity, installedAppList)

    @ActivityScope
    @Provides
    fun provideAppListAdapter(appListActivity: AppListActivity,
                              appListPresenter: AppListPresenter) = AppListAdapter(appListActivity, appListPresenter)

    @ActivityScope
    @Provides
    fun provideAppListStaggeredGridLayoutManager() = StaggeredGridLayoutManager(3, 1)

    @ActivityScope
    @Provides
    fun provideInstalledAppList(appListActivity: AppListActivity) =
            appListActivity.packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES)
}