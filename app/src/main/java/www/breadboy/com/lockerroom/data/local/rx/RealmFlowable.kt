package www.breadboy.com.lockerroom.data.local.rx

import io.reactivex.*
import io.reactivex.functions.Function
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults

/**
 * Created by N4039 on 2017-09-07.
 */

class RealmFlowable {

    fun <T : RealmObject> getRealmObject(function: Function<Realm, T>) = Flowable
            .create(object : RealmAppOnSubscribe<T>() {
                override fun get(realm: Realm) = function.apply(realm)
            }, BackpressureStrategy.BUFFER)

    fun <T : RealmObject> getRealmResult(function: Function<Realm, RealmResults<T>>) = Flowable
            .create(object : RealmAppOnSubscribe<RealmResults<T>>() {
                override fun get(realm: Realm): RealmResults<T> = function.apply(realm)
            }, BackpressureStrategy.BUFFER)
}