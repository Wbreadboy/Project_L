package www.breadboy.com.lockerroom.applist

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import dagger.Module
import dagger.Provides
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
    fun provideAppListPresenter(appListActivity: AppListActivity) = AppListPresenter(appListActivity)

    @ActivityScope
    @Provides
    fun provideAppListAdapter(appListActivity: AppListActivity) = AppListAdapter(appListActivity)

    @ActivityScope
    @Provides
    fun provideAppListStaggeredGridLayoutManager() = StaggeredGridLayoutManager(3, 1)
}