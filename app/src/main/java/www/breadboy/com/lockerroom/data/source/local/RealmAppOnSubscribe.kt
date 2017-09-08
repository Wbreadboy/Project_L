package www.breadboy.com.lockerroom.data.source.local

import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.realm.Realm
import io.realm.RealmObject

/**
 * Created by N4039 on 2017-09-08.
 */

abstract class RealmAppOnSubscribe<T> : FlowableOnSubscribe<T> {

    override fun subscribe(emitter: FlowableEmitter<T>) {
        val realm = Realm.getDefaultInstance()

        // TODO : in flowable
        try { realm.close() } catch (e: Exception) { emitter.onError(e) }

        val realmObj: T
        realm.beginTransaction()
        try {
            realmObj = get(realm)
            realm.commitTransaction()
        } catch (e: Exception) {
            realm.cancelTransaction()
            emitter.onError(e)

            return
        }

        emitter.onNext(realmObj)
        emitter.onComplete()
    }

    abstract fun get(realm: Realm): T
}