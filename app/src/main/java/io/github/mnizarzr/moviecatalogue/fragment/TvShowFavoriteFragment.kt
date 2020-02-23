package io.github.mnizarzr.moviecatalogue.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.mnizarzr.moviecatalogue.DetailFavoriteActivity
import io.github.mnizarzr.moviecatalogue.R
import io.github.mnizarzr.moviecatalogue.adapter.FavoriteAdapter
import io.github.mnizarzr.moviecatalogue.data.entity.Favorite
import io.github.mnizarzr.moviecatalogue.data.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.fragment_list_favorite.*

class TvShowFavoriteFragment : Fragment() {

    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list_favorite, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataAdapter = FavoriteAdapter(view.context)

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        favoriteViewModel.allTvFavorited.observe(this, Observer {
            if (it.isNotEmpty() && it != null) {
                dataAdapter.setData(it)
                txtState.visibility = View.GONE
            }
            else {
                rvFavorite.visibility = View.GONE
                txtState.visibility = View.VISIBLE
            }
        })

        rvFavorite.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = dataAdapter
        }

        dataAdapter.setOnItemClickCallback(object: FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Favorite) {
                Intent(context, DetailFavoriteActivity::class.java).apply {
                    putExtra(DetailFavoriteActivity.EXTRA_DATA, data)
                    putExtra(DetailFavoriteActivity.EXTRA_KIND, "tvShow")
                    startActivity(this)
                }
            }
        })

    }

}
