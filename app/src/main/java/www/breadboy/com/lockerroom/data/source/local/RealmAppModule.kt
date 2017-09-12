package www.breadboy.com.lockerroom.data.source.local

import android.util.Base64
import android.util.Log
import io.realm.RealmConfiguration
import io.realm.annotations.RealmModule
import www.breadboy.com.lockerroom.application.LockerRoomApplication
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey

/**
 * Created by N4039 on 2017-09-11.
 */

@RealmModule(classes = arrayOf(RealmApp::class))
class RealmAppModule {

    fun appConfig(): RealmConfiguration {
        val keyStore = KeyStore.getInstance(LockerRoomApplication.ANDROID_KEY_STORE).apply {

        }
        keyStore.load(null)

        return RealmConfiguration.Builder()
                .name(LockerRoomApplication.REALM_CONFIG_NAME_APPS)
                .encryptionKey((keyStore.getCertificate(LockerRoomApplication.ALIAS_KEY_REALM_APPS).encoded))
                .migration(RealmAppMigration())
                .schemaVersion(0)
                .modules(RealmAppModule())
                .build()
    }
}