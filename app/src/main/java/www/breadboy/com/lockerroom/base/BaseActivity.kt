package www.breadboy.com.lockerroom.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by SDG on 2017. 8. 22..
 */
abstract class BaseActivity<T> : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    abstract fun setupActivityComponent()

    fun startActionWithClickable(context: Context) {

    }
}