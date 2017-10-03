package www.breadboy.com.lockerroom.applist

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import www.breadboy.com.lockerroom.applist.view.AppListViewHolder
import www.breadboy.com.lockerroom.base.BaseActivity
import www.breadboy.com.lockerroom.base.BasePresenter
import www.breadboy.com.lockerroom.data.App

/**
 * Created by SDG on 2017. 8. 22..
 */

interface AppListContract {

    abstract class View : BaseActivity<Presenter>() {
        abstract fun setupRecyclerView()

        abstract fun showProgressBar()

        abstract fun hideProgressBar()
    }

    interface Presenter : BasePresenter {
        fun loadInstalledApps(page: Long): Disposable

        fun onInstalledAppClick(holder: AppListViewHolder?, position: Int, app: App): Disposable
    }
}