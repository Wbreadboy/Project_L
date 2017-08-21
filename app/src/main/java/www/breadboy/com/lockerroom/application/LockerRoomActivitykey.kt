package www.breadboy.com.lockerroom.application

import android.app.Activity
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Created by SDG on 2017. 8. 22..
 */

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LockerRoomActivitykey(val value: KClass<out Activity>)