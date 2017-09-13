package www.breadboy.com.lockerroom.data.local.realm

import io.realm.DynamicRealm
import io.realm.RealmMigration

/**
 * Created by N4039 on 2017-09-12.
 */

class RealmAppMigration : RealmMigration {

    override fun migrate(realm: DynamicRealm?, oldVersion: Long, newVersion: Long) {
        val schema = realm?.schema
        var oldVer = oldVersion

        when (oldVer) {
            0.toLong() -> {
                oldVer++
            }
        }
    }
}