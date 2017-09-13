package www.breadboy.com.lockerroom.data.local.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by N4039 on 2017-09-07.
 */

open class RealmApp : RealmObject() {

    @PrimaryKey
    var packageName: String? = null
    var iconId = -1
    var appName: String? = null
    var locked = false
}