package www.breadboy.com.lockerroom.data.local.realm

import io.realm.RealmConfiguration
import io.realm.annotations.RealmModule
import www.breadboy.com.lockerroom.application.LockerRoomApplication

/**
 * Created by N4039 on 2017-09-11.
 */

@RealmModule(classes = arrayOf(RealmApp::class))
class RealmAppModule {

    fun appConfig(): RealmConfiguration = RealmConfiguration.Builder()
            .name(LockerRoomApplication.REALM_CONFIG_NAME_APPS)
            .encryptionKey(LockerRoomApplication.REALM_ENCRYPTION_KEY_APPS)
            .migration(RealmAppMigration())
            .schemaVersion(0)
            .modules(this)
            .build()
}