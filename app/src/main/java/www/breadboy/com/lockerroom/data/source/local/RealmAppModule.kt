package www.breadboy.com.lockerroom.data.source.local

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import io.realm.RealmConfiguration
import io.realm.annotations.RealmModule
import java.security.CryptoPrimitive
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Created by N4039 on 2017-09-11.
 */

@RealmModule(classes = arrayOf(RealmApp::class))
class RealmAppModule {

    fun appConfig() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(KeyGenParameterSpec.Builder("key1", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setKeySize(256)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(true)
                .setUserAuthenticationRequired(true)
                .setUserAuthenticationValidityDurationSeconds(5 * 60)
                .build())

        val secretKey = keyGenerator.generateKey()

        // retrieval
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val entry = keyStore.getEntry("key1", null) as KeyStore.SecretKeyEntry
        val key = keyStore.getCertificate("key1").encoded

        RealmConfiguration.Builder()
                .name("locked_apps.realm")
                .encryptionKey(key)
                .schemaVersion(1)
                .modules(RealmAppModule())
                .build()
    }
}