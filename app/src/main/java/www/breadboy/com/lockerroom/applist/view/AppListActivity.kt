package www.breadboy.com.lockerroom.applist.view

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.content_app_list.*
import www.breadboy.com.lockerroom.R
import www.breadboy.com.lockerroom.application.LockerRoomApplication
import www.breadboy.com.lockerroom.applist.AppListContract
import www.breadboy.com.lockerroom.applist.di.AppListComponent
import www.breadboy.com.lockerroom.applist.di.AppListModule
import www.breadboy.com.lockerroom.applist.presenter.AppListPresenter
import www.breadboy.com.lockerroom.data.App
import javax.inject.Inject

class AppListActivity : AppListContract.View() {

    @Inject
    lateinit var appListPresenter: AppListPresenter

    @Inject
    lateinit var appListAdapter: AppListAdapter

    @Inject
    lateinit var appListStaggeredGridLayoutManager: StaggeredGridLayoutManager

    companion object {
        const val ADJUSTED_VALUE = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_app_list)
        setupActivityComponent()
        setupRecyclerView()

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
        }

        appListPresenter.start()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_app_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun setupActivityComponent() {
        (LockerRoomApplication[this].getComponentBuilder(AppListActivity::class.java) as AppListComponent.Builder)
                .module(AppListModule(this))
                .build()
                .injectMembers(this)
    }

    var isLoading = false
    override fun setupRecyclerView() {
        var page: Long = 1

        recyclerview_app_list_activity.apply {
            adapter = appListAdapter
            layoutManager = appListStaggeredGridLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (!isLoading)
                        appListStaggeredGridLayoutManager.apply {
                            if (childCount + findFirstVisibleItemPositions(null).first() + 5 >= itemCount) {
                                showProgressBar()

                                appListPresenter.loadInstalledApps(page++)
                            }
                        }
                }
            })
        }
    }

    fun addAppsToRecyclerView(apps: List<App>) { appListAdapter.addApps(apps) }

    override fun showProgressBar() {
        if (progressbar_app_list_activity.visibility != View.VISIBLE) {
            progressbar_app_list_activity.visibility = View.VISIBLE
        }
    }

    override fun hideProgressBar() {
        progressbar_app_list_activity.visibility = View.GONE
    }
}
