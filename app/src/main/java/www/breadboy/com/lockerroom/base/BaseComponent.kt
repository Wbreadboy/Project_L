package www.breadboy.com.lockerroom.base

import android.app.Activity
import dagger.MembersInjector

/**
 * Created by SDG on 2017. 8. 22..
 */

interface BaseComponent<A: Activity> : MembersInjector<A>