package www.breadboy.com.lockerroom.base

import android.app.Activity
import dagger.Module

/**
 * Created by SDG on 2017. 8. 22..
 */

@Module
abstract class BaseModule<out A : Activity>(val activity: A)