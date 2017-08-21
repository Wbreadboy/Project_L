package www.breadboy.com.lockerroom.application

import android.app.Application
import android.content.Context
import dagger.Component
import javax.inject.Singleton

/**
 * Created by SDG on 2017. 8. 22..
 */

@Singleton
@Component(modules = arrayOf(LockerRoomModule::class))
interface LockerRoomComponent {

    fun inject(lockerRoomApplication: LockerRoomApplication): LockerRoomApplication
}