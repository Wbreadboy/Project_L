package www.breadboy.com.lockerroom.applist.di

import dagger.Subcomponent
import www.breadboy.com.lockerroom.applist.ActivityScope
import www.breadboy.com.lockerroom.applist.view.AppListActivity
import www.breadboy.com.lockerroom.base.BaseComponent
import www.breadboy.com.lockerroom.base.ComponentBuilder

/**
 * Created by SDG on 2017. 8. 22..
 */

@ActivityScope
@Subcomponent(modules = arrayOf(AppListModule::class))
interface AppListComponent : BaseComponent<AppListActivity> {

    @Subcomponent.Builder
    interface Builder : ComponentBuilder<AppListModule, AppListComponent>
}