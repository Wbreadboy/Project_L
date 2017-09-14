package www.breadboy.com.lockerroom.data.local.rx

import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.realm.Realm
import io.realm.RealmObject
import www.breadboy.com.lockerroom.application.LockerRoomApplication
import www.breadboy.com.lockerroom.data.local.realm.RealmAppModule

/**
 * Created by N4039 on 2017-09-08.
 */

abstract class RealmAppOnSubscribe<T> : FlowableOnSubscribe<T> {

    override fun subscribe(emitter: FlowableEmitter<T>) {
        var realmObj: T? = null

        // TODO : Injection 필요 - RealmAppModule
        Realm.getInstance(LockerRoomApplication.realmAppConfig). let {
            // TODO : in flowable
            emitter.setCancellable {

            }

            it.beginTransaction()

            try {
                realmObj = get(it)

                emitter.onNext(realmObj!!)
                emitter.onComplete()

                it.commitTransaction()
            } catch (e: Exception) {
                it.cancelTransaction()
                emitter.onError(e)

                return
            }

            /*try {
                it.close()
            } catch (e: Exception) {
                emitter.onError(e)
            }*/
        }
    }

    abstract fun get(realm: Realm): T
}