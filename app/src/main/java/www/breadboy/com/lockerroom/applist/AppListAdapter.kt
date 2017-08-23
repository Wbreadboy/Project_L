package www.breadboy.com.lockerroom.applist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import www.breadboy.com.lockerroom.R
import www.breadboy.com.lockerroom.data.App

/**
 * Created by N4039 on 2017-08-23.
 */
class AppListAdapter : RecyclerView.Adapter<AppListViewHolder>() {

    private val appListMutableList: MutableList<App> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            AppListViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.applist_cardview, parent, false))

    override fun onBindViewHolder(holder: AppListViewHolder?, position: Int) {
        holder?.bind()

        // TODO : Set app icon image with Glide

        // TODO : Set app name
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}