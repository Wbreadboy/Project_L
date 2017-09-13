package www.breadboy.com.lockerroom.data.local.di

import dagger.Subcomponent
import www.breadboy.com.lockerroom.base.ComponentBuilder
import javax.inject.Singleton

/**
 * Created by N4039 on 2017-09-13.
 */

@Singleton
@Subcomponent(modules = arrayOf(AppsLocalModule::class))
interface AppsLocalComponent {

    //@Subcomponent.Builder
    //interface Builder : ComponentBuilder<AppsLocalModule, AppsLocalComponent>
}