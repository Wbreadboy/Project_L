package www.breadboy.com.lockerroom.base

import android.app.Activity

/**
 * Created by SDG on 2017. 8. 22..
 */
interface WhichSubcomponentBuilders {

    fun getComponentBuilder(activity: Class<out Activity>): ComponentBuilder<*, *>
}