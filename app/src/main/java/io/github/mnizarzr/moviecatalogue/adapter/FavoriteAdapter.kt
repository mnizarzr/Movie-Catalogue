package io.github.mnizarzr.moviecatalogue.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.RoundedCornersTransformation
import io.github.mnizarzr.moviecatalogue.R
import io.github.mnizarzr.moviecatalogue.data.entity.Favorite
import io.github.mnizarzr.moviecatalogue.utils.inflate
import kotlinx.android.synthetic.main.item_movie.view.*

class FavoriteAdapter(private val mContext: Context) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private var favorites = listOf<Favorite>()

    interface OnItemClickCallback {
        fun onItemClicked(data: Favorite)
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(favorites: List<Favorite>) {
        this.favorites = favorites
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_movie))

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Favorite) {
            val overview: String = if (data.overview != "") data.overview
            else mContext.resources.getString(R.string.text_no_overview)
            with(itemView) {
                txtTitle.text = data.title
                txtOverview.text = overview
                if (data.posterPath == null || data.posterPath.isBlank()) {
                    imgMovie.load(R.drawable.cover_not_available) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(16f))
                    }
                } else {
                    imgMovie.load(data.getPosterUrl()) {
                        transformations(RoundedCornersTransformation(16f))
                        crossfade(true)
                    }
                }
                setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }
    }

}