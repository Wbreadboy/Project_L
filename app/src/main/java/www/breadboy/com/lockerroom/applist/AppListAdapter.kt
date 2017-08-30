package www.breadboy.com.lockerroom.applist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import www.breadboy.com.lockerroom.R
import www.breadboy.com.lockerroom.application.GlideApp
import www.breadboy.com.lockerroom.data.App
import javax.inject.Inject


/**
 * Created by N4039 on 2017-08-23.
 */

class AppListAdapter

@Inject
constructor(val appListActivity: AppListActivity): RecyclerView.Adapter<AppListViewHolder>() {

    private val mutableAppList: MutableList<App> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            AppListViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.applist_cardview, parent, false))

    override fun onBindViewHolder(holder: AppListViewHolder?, position: Int) {
        holder?.bind()

        GlideApp.with(appListActivity)
                .load("android.resource://${mutableAppList[position].appPackageName}/${mutableAppList[position].appIconId}")
                .fitCenter()
                .error(R.mipmap.ic_launcher)
                .into(holder?.appIconImageView)

        holder?.appNameTextView?.text = mutableAppList[position].appName
    }

    fun addApp(app: App) = mutableAppList.add(app)

    fun addApps(apps: List<App>) = mutableAppList.addAll(apps)

    fun removeApp(app: App) = mutableAppList.remove(app)

    fun clearApp() = mutableAppList.clear()

    override fun getItemCount() = mutableAppList.size
}