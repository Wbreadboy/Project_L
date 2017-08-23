package www.breadboy.com.lockerroom.applist

import android.content.Context
import www.breadboy.com.lockerroom.base.BaseActivity
import www.breadboy.com.lockerroom.base.BasePresenter

/**
 * Created by SDG on 2017. 8. 22..
 */

interface AppListContract {

    abstract class View : BaseActivity<Presenter>()

    interface Presenter : BasePresenter
}