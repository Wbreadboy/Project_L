package www.breadboy.com.lockerroom.applist

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.content_app_list.*
import www.breadboy.com.lockerroom.R
import www.breadboy.com.lockerroom.application.LockerRoomApplication
import www.breadboy.com.lockerroom.data.App
import javax.inject.Inject

class AppListActivity : AppListContract.View() {

    @Inject
    lateinit var appListPresenter: AppListPresenter

    @Inject
    lateinit var appListAdapter: AppListAdapter

    @Inject
    lateinit var appListStaggeredGridLayoutManager: StaggeredGridLayoutManager

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
    }

    override fun onResume() {
        super.onResume()

        appListPresenter.start()
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

    private fun setupRecyclerView() {
        recyclerview_app_list_activity.adapter = appListAdapter
        recyclerview_app_list_activity.layoutManager = appListStaggeredGridLayoutManager
        recyclerview_app_list_activity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var totalItemCount = appListStaggeredGridLayoutManager.itemCount
                var lastVisibleItem = appListStaggeredGridLayoutManager.findLastVisibleItemPositions(null)[0]

                if (totalItemCount <= lastVisibleItem + 5) {
                    Log.e("!!!!!!!!!!!!!!!!", "$totalItemCount            $lastVisibleItem")
                    appListPresenter.getInstalledAppsByParts(appListPresenter.appListStartIdx).subscribe({ app -> appListAdapter.addApp(app) })
                    appListPresenter.appListStartIdx += AppListPresenter.MAX_LOADING_APP_LEN
                }
            }
        } )
    }
}
