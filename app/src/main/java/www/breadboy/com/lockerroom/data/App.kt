package www.breadboy.com.lockerroom.data

import dagger.multibindings.IntoMap

/**
 * Created by N4039 on 2017-08-23.
 */

data class App(
        var appPackageName: String,
        var appIconId: Int,
        var appName: String,
        var isLocked: Boolean)