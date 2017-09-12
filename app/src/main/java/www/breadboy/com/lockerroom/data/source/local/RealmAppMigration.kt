package www.breadboy.com.lockerroom.data.source.local

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
                schema?.create("App")
                        ?.addField("package_name", String::class.java)
                        ?.addField("icon_id", Int::class.java)
                        ?.addField("app_name", String::class.java)
                        ?.addField("locked", Boolean::class.java)

                oldVer++
            }
        }
    }
}