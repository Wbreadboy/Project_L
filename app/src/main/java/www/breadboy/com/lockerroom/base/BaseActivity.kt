package www.breadboy.com.lockerroom.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by SDG on 2017. 8. 22..
 */
abstract class BaseActivity<T> : AppCompatActivity() {

    protected val disposables by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onDestroy() {
        disposables.clear()

        super.onDestroy()
    }

    abstract fun setupActivityComponent()
}