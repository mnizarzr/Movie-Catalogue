package io.github.mnizarzr.moviecatalogue.fragment


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.mnizarzr.moviecatalogue.DetailActivity
import io.github.mnizarzr.moviecatalogue.R
import io.github.mnizarzr.moviecatalogue.SettingsActivity
import io.github.mnizarzr.moviecatalogue.adapter.DataAdapter
import io.github.mnizarzr.moviecatalogue.data.model.ItemResult
import io.github.mnizarzr.moviecatalogue.data.viewmodel.TvShowViewModel
import io.github.mnizarzr.moviecatalogue.utils.Constants.KIND_TVSHOW
import io.github.mnizarzr.moviecatalogue.utils.hide
import io.github.mnizarzr.moviecatalogue.utils.show
import kotlinx.android.synthetic.main.fragment_movie.*

class TvShowFragment : Fragment() {

    private lateinit var tvShowAdapter: DataAdapter
    private lateinit var tvShowViewModel: TvShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        tvShowViewModel.getTvShows().observe(viewLifecycleOwner, Observer {
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
                    putExtra(DetailActivity.EXTRA_KIND, KIND_TVSHOW)
                    startActivity(this)
                }
            }

        })

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.show()
            recyclerView.hide()
        } else {
            progressBar.hide()
            recyclerView.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                tvShowViewModel.searchTvShows(query).observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        tvShowAdapter.setData(it)
                        showLoading(false)
                    }
                })
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isBlank()) {
                    tvShowViewModel.getTvShows().also {
                        tvShowAdapter.setData(it.value)
                    }
                } else {
                    showLoading(true)
                    tvShowViewModel.searchTvShows(newText).observe(viewLifecycleOwner, Observer {
                        if (it != null) {
                            tvShowAdapter.setData(it)
                            showLoading(false)
                        }
                    })
                }
                return true
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.action_settings -> startActivity(Intent(context, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


}
