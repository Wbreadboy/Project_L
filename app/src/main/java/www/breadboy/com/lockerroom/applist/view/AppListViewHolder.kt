package www.breadboy.com.lockerroom.applist.view

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.applist_cardview.view.*

/**
 * Created by N4039 on 2017-08-23.
 */
class AppListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var appCardView: CardView
    lateinit var appIconImageView: ImageView
    lateinit var appNameTextView: TextView

    fun bind() = with(itemView) {
        appCardView = cardview_appcard_cardview_applist
        appIconImageView = imageview_appicon_cardview_applist
        appNameTextView = textview_appname_cardview_applist
    }
}