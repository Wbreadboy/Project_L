package www.breadboy.com.lockerroom.applist

import android.content.ComponentName
import android.content.pm.ComponentInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.applist_cardview.*
import www.breadboy.com.lockerroom.R
import www.breadboy.com.lockerroom.data.App
import javax.inject.Inject
import android.content.pm.PackageManager.GET_SERVICES
import android.content.pm.PackageManager.GET_RECEIVERS
import android.content.pm.PackageManager.GET_PROVIDERS
import android.content.pm.PackageManager.GET_ACTIVITIES
import www.breadboy.com.lockerroom.application.GlideApp


/**
 * Created by N4039 on 2017-08-23.
 */

class AppListAdapter

@Inject
constructor(val appListActivity: AppListActivity): RecyclerView.Adapter<AppListViewHolder>() {

    private val mutableAppList: MutableList<App> = arrayListOf()
    private val packageManager = appListActivity.packageManager

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            AppListViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.applist_cardview, parent, false))

    override fun onBindViewHolder(holder: AppListViewHolder?, position: Int) {
        holder?.bind()

        val appPackageName = mutableAppList[position].appPackageName

        GlideApp.with(appListActivity)
                .load("android.resource://$appPackageName/${mutableAppList[position].appIconId}")
                .fitCenter()
                .error(R.mipmap.ic_launcher)
                .into(holder?.appIconImageView)

        holder?.appNameTextView?.text = packageManager.getApplicationLabel(packageManager.getApplicationInfo(appPackageName, 0))
    }

    fun addApp(app: App) = mutableAppList.add(app)

    fun addApps(apps: List<App>) = mutableAppList.addAll(apps)

    fun removeApp(app: App) = mutableAppList.remove(app)

    fun clearApp() = mutableAppList.clear()

    override fun getItemCount() = mutableAppList.size
}