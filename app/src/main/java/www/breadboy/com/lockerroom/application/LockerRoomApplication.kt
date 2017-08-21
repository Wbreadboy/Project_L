package www.breadboy.com.lockerroom.application

import android.app.Activity
import android.app.Application
import android.content.Context
import www.breadboy.com.lockerroom.base.ComponentBuilder
import www.breadboy.com.lockerroom.base.WhichSubcomponentBuilders
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by SDG on 2017. 8. 22..
 */

class LockerRoomApplication : Application(), WhichSubcomponentBuilders {

    @Inject
    lateinit var lockerRoomComponentBuilders: Map<Class<out Activity>, Provider<ComponentBuilder<*, *>>>

    override fun onCreate() {
        super.onCreate()

        initLockerRoomModule()
    }

    private fun initLockerRoomModule() {
        DaggerLockerRoomComponent.create().inject(this)
    }

    override fun getComponentBuilder(activity: Class<out Activity>) = lockerRoomComponentBuilders[activity]!!.get()

    companion object {
        operator fun get(context: Context) = context.applicationContext as WhichSubcomponentBuilders
    }
}