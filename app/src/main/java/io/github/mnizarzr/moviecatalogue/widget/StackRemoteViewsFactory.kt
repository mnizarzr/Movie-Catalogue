package io.github.mnizarzr.moviecatalogue.widget

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import io.github.mnizarzr.moviecatalogue.R
import io.github.mnizarzr.moviecatalogue.data.database.AppDatabase
import io.github.mnizarzr.moviecatalogue.data.entity.Favorite

class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = arrayListOf<Favorite>()
    private var database = AppDatabase.getDatabase(mContext)

    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        val list = database.favoriteDao().getAll()
        for (position in list.indices) {
            mWidgetItems.add(list[position])
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun hasStableIds(): Boolean = true

    override fun getViewAt(position: Int): RemoteViews {
        val item = mWidgetItems[position]
        val remoteViews = RemoteViews(mContext.packageName, R.layout.widget_item)
//        val uri = Uri.parse(mWidgetItems[position].getPosterUrl())
        val imageBitmap = Glide.with(mContext)
            .asBitmap()
            .load(item.getPosterUrl())
            .submit()
            .get()
        remoteViews.setImageViewBitmap(
            R.id.imageView,
            imageBitmap
        )
        val extras = bundleOf(FavoriteBannerWidget.EXTRA_ITEM to item.title)
        val fillInIntent = Intent().apply { putExtras(extras) }
        remoteViews.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return remoteViews
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
        mWidgetItems.clear()
    }

}