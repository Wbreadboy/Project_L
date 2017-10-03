package www.breadboy.com.lockerroom.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.provider.Settings
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.log.RealmLog
import www.breadboy.com.lockerroom.base.ComponentBuilder
import www.breadboy.com.lockerroom.base.WhichSubcomponentBuilders
import www.breadboy.com.lockerroom.data.local.realm.RealmAppModule
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by SDG on 2017. 8. 22..
 */

class LockerRoomApplication : Application(), WhichSubcomponentBuilders {

    @Inject
    lateinit var lockerRoomComponentBuilders: Map<Class<out Activity>, @JvmSuppressWildcards Provider<ComponentBuilder<*, *>>>

    companion object {
        const val REALM_CONFIG_NAME_APPS = "locked_apps.realm"
        lateinit var REALM_ENCRYPTION_KEY_APPS: ByteArray
        lateinit var realmAppConfig: RealmConfiguration

        operator fun get(context: Context): WhichSubcomponentBuilders {
            return context.applicationContext as WhichSubcomponentBuilders
        }
    }

    override fun onCreate() {
        super.onCreate()

        setupLockerRoomModule()
        setupRealm()
        setupRealmKey()
        setupRealmConfiguration()
    }

    private fun setupLockerRoomModule() = DaggerLockerRoomComponent.create().inject(this)

    private fun setupRealm() {
        Realm.init(this)
        RealmLog.setLevel(Log.VERBOSE)
    }

    private fun setupRealmKey() {
        REALM_ENCRYPTION_KEY_APPS = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID).dropLast(1).toByteArray(Charsets.UTF_32)
    }

    private fun setupRealmConfiguration() {
        realmAppConfig = RealmAppModule().appConfig()
    }

    override fun getComponentBuilder(activity: Class<out Activity>) = lockerRoomComponentBuilders[activity]!!.get()
}