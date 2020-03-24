package io.github.mnizarzr.consumer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.RoundedCornersTransformation
import io.github.mnizarzr.consumer.entity.Favorite
import kotlinx.android.synthetic.main.item_movie.view.*

class DataAdapter(private val mContext: Context) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    private var favorites = listOf<Favorite>()

    fun setData(favorites: List<Favorite>) {
        this.favorites = favorites
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Favorite) {
            with(itemView) {
                txtTitle.text = data.title
                txtOverview.text = data.overview
                imgMovie.load(data.getPosterUrl()) {
                    transformations(RoundedCornersTransformation(16f))
                    crossfade(true)
                }
            }
        }
    }
}