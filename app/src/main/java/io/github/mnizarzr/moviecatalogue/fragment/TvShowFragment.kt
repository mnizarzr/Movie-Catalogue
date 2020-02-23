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
import io.github.mnizarzr.moviecatalogue.DetailActivity
import io.github.mnizarzr.moviecatalogue.R
import io.github.mnizarzr.moviecatalogue.adapter.DataAdapter
import io.github.mnizarzr.moviecatalogue.data.model.ItemResult
import io.github.mnizarzr.moviecatalogue.data.viewmodel.TvShowViewModel
import io.github.mnizarzr.moviecatalogue.utils.hide
import io.github.mnizarzr.moviecatalogue.utils.show
import kotlinx.android.synthetic.main.fragment_movie.*

class TvShowFragment : Fragment() {

    private lateinit var tvShowAdapter: DataAdapter
    private lateinit var tvShowViewModel: TvShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tvshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvShowAdapter = DataAdapter(view.context)
        tvShowAdapter.notifyDataSetChanged()

        tvShowViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(TvShowViewModel::class.java)

        showLoading(true)
        tvShowViewModel.setTvShows()

        tvShowViewModel.getTvShows().observe(this, Observer {
            if (it != null) {
                tvShowAdapter.setData(it)
                showLoading(false)
            }
        })

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tvShowAdapter
        }

        tvShowAdapter.setOnItemClickCallback(object : DataAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemResult) {
                Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_DATA, data)
                    putExtra(DetailActivity.EXTRA_KIND, "tvShow")
                    startActivity(this)
                }
            }

        })

    }

    private fun showLoading(state: Boolean) {
        if (state) progressBar.show()
        else progressBar.hide()
    }


}
