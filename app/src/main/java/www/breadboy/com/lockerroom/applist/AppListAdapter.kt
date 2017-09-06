package www.breadboy.com.lockerroom.applist

import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
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
constructor(val appListActivity: AppListActivity,
            val appListPresenter: AppListContract.Presenter): RecyclerView.Adapter<AppListViewHolder>() {

    private val mutableAppList: MutableList<App> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            AppListViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.applist_cardview, parent, false))

    override fun onBindViewHolder(holder: AppListViewHolder?, position: Int) {
        holder?.bind()

        setAppLayoutInLinearLayout(holder, position)
        setAppIconInImageView(holder, position)
        setAppNameInTextView(holder, position)
    }

    fun addApp(app: App) = mutableAppList.add(app)

    fun addApps(apps: List<App>) = mutableAppList.addAll(apps)

    fun removeApp(app: App) = mutableAppList.remove(app)

    fun clearApp() = mutableAppList.clear()

    override fun getItemCount() = mutableAppList.size

    private fun setAppLayoutInLinearLayout(holder: AppListViewHolder?, position: Int) {
        holder?.appCardView?.setOnClickListener { onAppListClicked(holder, position, mutableAppList.get(position)) }
    }

    private fun setAppIconInImageView(holder: AppListViewHolder?, position: Int) {
        GlideApp.with(appListActivity)
                .load("android.resource://${mutableAppList[position].appPackageName}/${mutableAppList[position].appIconId}")
                .fitCenter()
                .error(R.mipmap.ic_launcher)
                .into(holder?.appIconImageView)
    }

    private fun setAppNameInTextView(holder: AppListViewHolder?, position: Int) {
        holder?.appNameTextView?.text = mutableAppList[position].appName
    }

    fun onAppListClicked(holder: AppListViewHolder?, position: Int, app: App) {
        appListPresenter.onInstalledAppClick(holder, position, app)
    }

    fun wrapLockIconToLayout(holder: AppListViewHolder?, position: Int) {
        GlideApp.with(appListActivity)
                .load(R.drawable.ic_locked_icon)
                .fitCenter()
                .error(R.drawable.ic_locked_icon)
                .into(holder?.appIconImageView)

        holder?.appCardView?.setCardBackgroundColor(ContextCompat.getColor(appListActivity, android.R.color.black))
        holder?.appNameTextView?.setTextColor(ContextCompat.getColor(appListActivity, android.R.color.white))
    }
}