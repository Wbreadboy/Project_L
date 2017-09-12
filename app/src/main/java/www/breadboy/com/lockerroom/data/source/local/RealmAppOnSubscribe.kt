package www.breadboy.com.lockerroom.data.source.local

import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.RealmModule

/**
 * Created by N4039 on 2017-09-08.
 */

abstract class RealmAppOnSubscribe<T> : FlowableOnSubscribe<T> {

    override fun subscribe(emitter: FlowableEmitter<T>) {
        var realmObj: T? = null

        // TODO : Injection 필요 - RealmAppModule
        Realm.getInstance(RealmAppModule().appConfig()). let {
            // TODO : in flowable
            try { it.close() } catch (e: Exception) { emitter.onError(e) }

            it.beginTransaction()
            try {
                realmObj = get(it)
                it.commitTransaction()
            } catch (e: Exception) {
                it.cancelTransaction()
                emitter.onError(e)

                return
            }
        }

        emitter.onNext(realmObj!!)
        emitter.onComplete()
    }

    abstract fun get(realm: Realm): T
}