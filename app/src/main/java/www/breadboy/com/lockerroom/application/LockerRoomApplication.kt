package www.breadboy.com.lockerroom.application

import android.animation.Keyframe
import android.app.Activity
import android.app.Application
import android.content.Context
import android.provider.Settings
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import io.realm.Realm
import io.realm.log.RealmLog
import www.breadboy.com.lockerroom.base.ComponentBuilder
import www.breadboy.com.lockerroom.base.WhichSubcomponentBuilders
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Signature
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
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

        operator fun get(context: Context): WhichSubcomponentBuilders {
            return context.applicationContext as WhichSubcomponentBuilders
        }
    }

    override fun onCreate() {
        super.onCreate()

        setupLockerRoomModule()
        setupRealm()
        setupRealmKey()
    }

    private fun setupLockerRoomModule() = DaggerLockerRoomComponent.create().inject(this)

    private fun setupRealm() {
        Realm.init(this)
        RealmLog.setLevel(Log.VERBOSE)
    }

    private fun setupRealmKey() {
        REALM_ENCRYPTION_KEY_APPS = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID).dropLast(1).toByteArray(Charsets.UTF_32)
    }

    override fun getComponentBuilder(activity: Class<out Activity>) = lockerRoomComponentBuilders[activity]!!.get()
}