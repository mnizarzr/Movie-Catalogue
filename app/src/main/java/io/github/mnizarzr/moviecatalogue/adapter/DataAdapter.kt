package io.github.mnizarzr.moviecatalogue.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.RoundedCornersTransformation
import io.github.mnizarzr.moviecatalogue.R
import io.github.mnizarzr.moviecatalogue.data.model.ItemResult
import io.github.mnizarzr.moviecatalogue.utils.inflate
import kotlinx.android.synthetic.main.item_movie.view.*

class DataAdapter(private val mContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM_TOP = 0
        private const val ITEM_REGULAR = 1
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemResult)
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val listMovies = ArrayList<ItemResult>()

    fun setData(items: ArrayList<ItemResult>) {
        listMovies.clear()
        listMovies.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_TOP
            else -> ITEM_REGULAR
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TOP -> TopViewHolder(viewGroup.inflate(R.layout.item_top_movie))
            ITEM_REGULAR -> RegularViewHolder(viewGroup.inflate(R.layout.item_movie))
            else -> throw IllegalArgumentException("ViewType not supported")
        }
    }

    override fun getItemCount(): Int = listMovies.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OnBind).bind(listMovies[position])
    }

    inner class RegularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnBind {
        override fun bind(data: ItemResult) {
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
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }
    }

    inner class TopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnBind {
        override fun bind(data: ItemResult) {
            val overview: String = if (data.overview != "") data.overview
            else mContext.resources.getString(R.string.text_no_overview)
            with(itemView) {
                txtTitle.text = data.title
                txtOverview.text = overview
                if (data.backdropPath == null || data.backdropPath.isBlank()) {
                    imgMovie.load(R.drawable.cover_not_available) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(16f))
                    }
                } else {
                    imgMovie.load(data.getBackdropUrl()) {
                        transformations(RoundedCornersTransformation(16f))
                        crossfade(true)
                    }
                }
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }
    }

    interface OnBind {
        fun bind(data: ItemResult)
    }

}