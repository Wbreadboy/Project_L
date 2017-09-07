package www.breadboy.com.lockerroom.application

import android.app.Activity
import android.app.Application
import android.content.Context
import io.realm.Realm
import www.breadboy.com.lockerroom.base.ComponentBuilder
import www.breadboy.com.lockerroom.base.WhichSubcomponentBuilders
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by SDG on 2017. 8. 22..
 */

class LockerRoomApplication : Application(), WhichSubcomponentBuilders {

    @Inject
    lateinit var lockerRoomComponentBuilders: Map<Class<out Activity>, @JvmSuppressWildcards Provider<ComponentBuilder<*, *>>>

    override fun onCreate() {
        super.onCreate()

        setupLockerRoomModule()
        setupRealm()
    }

    private fun setupLockerRoomModule() = DaggerLockerRoomComponent.create().inject(this)

    private fun setupRealm() = Realm.init(this)

    override fun getComponentBuilder(activity: Class<out Activity>) = lockerRoomComponentBuilders[activity]!!.get()

    companion object {
        operator fun get(context: Context): WhichSubcomponentBuilders {
            return context.applicationContext as WhichSubcomponentBuilders
        }
    }
}