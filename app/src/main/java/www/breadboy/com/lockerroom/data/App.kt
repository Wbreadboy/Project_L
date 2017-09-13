package www.breadboy.com.lockerroom.data

import dagger.multibindings.IntoMap

/**
 * Created by N4039 on 2017-08-23.
 */

data class App(
        var packageName: String?,
        var iconId: Int,
        var appName: String?,
        var locked: Boolean)