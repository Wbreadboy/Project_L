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
        const val ANDROID_KEY_STORE = "AndroidKeyStore"
        const val ALIAS_KEY_REALM_APPS = "locked_apps.realm.android_key_store"
        const val REALM_CONFIG_NAME_APPS = "locked_apps.realm"

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
        KeyStore.getInstance(ANDROID_KEY_STORE)
                .apply {
                    load(null)
                    getCertificate(ALIAS_KEY_REALM_APPS)?.encoded ?: generateSecretKey(ALIAS_KEY_REALM_APPS)

                    Log.e("!!!!!!!!!!!!!!!!!!", "${getCertificate(ALIAS_KEY_REALM_APPS)?.encoded}")
                }
    }

    private fun generateSecretKey(alias: String) {
        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE).apply {
            init(KeyGenParameterSpec
                    .Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    //.setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    //.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build())

            val secretKey = generateKey()
        }



            KeyStore.getInstance(ANDROID_KEY_STORE)
                    .apply {
                        load(null)

                        Log.e("!!!!!!!!!!!!!!!!!!", "gen ${getCertificate(ALIAS_KEY_REALM_APPS)?.encoded}")
                    }
        }

        /*KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, ANDROID_KEY_STORE).apply {
            initialize(KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setKeySize(512)
                    .setDigests(KeyProperties.DIGEST_SHA512)
                    .build())

            generateKeyPair()*/


            /*val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)

            val signature = Signature.getInstance("SHA256withECDSA")
            signature.initSign((keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry).privateKey)


            Log.e("!!!!!!!!!!!!!!!!!!", "${signature.sign()}")*/



    override fun getComponentBuilder(activity: Class<out Activity>) = lockerRoomComponentBuilders[activity]!!.get()
}