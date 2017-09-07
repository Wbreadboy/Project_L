package www.breadboy.com.lockerroom.data.source.local

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.realm.Realm

/**
 * Created by N4039 on 2017-09-07.
 */

class RealmAppFlowable {

    fun getRealmAppFlowable() = Flowable
            .create<Realm>({ return it. }
                    , BackpressureStrategy.BUFFER)
}