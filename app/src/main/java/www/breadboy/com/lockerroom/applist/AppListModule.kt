package www.breadboy.com.lockerroom.applist

import dagger.Module
import www.breadboy.com.lockerroom.base.BaseModule

/**
 * Created by SDG on 2017. 8. 22..
 */

@Module
class AppListModule(var appListActivity: AppListActivity) : BaseModule<AppListActivity>(appListActivity) {

}