package www.breadboy.com.lockerroom.application

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import www.breadboy.com.lockerroom.applist.AppListActivity
import www.breadboy.com.lockerroom.applist.AppListComponent
import www.breadboy.com.lockerroom.base.ComponentBuilder

/**
 * Created by SDG on 2017. 8. 22..
 */

@Module(subcomponents = arrayOf(AppListComponent::class))
abstract class LockerRoomModule {

    @Binds
    @IntoMap
    @LockerRoomActivityKey(AppListActivity::class)
    abstract fun bindAppListActivity(impl: AppListComponent.Builder): ComponentBuilder<*, *>
}