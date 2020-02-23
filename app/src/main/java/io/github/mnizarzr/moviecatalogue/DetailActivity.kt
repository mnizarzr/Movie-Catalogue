package io.github.mnizarzr.moviecatalogue

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import io.github.mnizarzr.moviecatalogue.data.entity.Favorite
import io.github.mnizarzr.moviecatalogue.data.model.ItemResult
import io.github.mnizarzr.moviecatalogue.data.viewmodel.FavoriteViewModel
import io.github.mnizarzr.moviecatalogue.utils.showToast
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.SimpleDateFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var data: ItemResult
    private var isFavorite: Boolean = false

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
        const val EXTRA_KIND = "EXTRA_KIND"
        const val DATE = "yyyy-MM-dd"
        const val FORMATTED_DATE = "dd MMMM yyyy"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favoriteViewModel = ViewModelProvider(
            this
        ).get(FavoriteViewModel::class.java)

        val data = intent.getParcelableExtra(EXTRA_DATA) as ItemResult
        this.data = data

        favoriteViewModel.checkFavorite(data.id).observe(this, Observer {
            isFavorite = it > 0
        })

        val date = SimpleDateFormat(DATE).parse(data.releaseDate)!!
        val overview: String =
            if (data.overview.isNotEmpty()) data.overview else resources.getString(R.string.text_no_overview)
        txtTitle.text = data.title
        txtOverview.text = overview
        txtReleaseDate.text = SimpleDateFormat(FORMATTED_DATE).format(date)
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

        if (data.backdropPath == null || data.backdropPath.isBlank()) {
            imgBackdrop.load(R.drawable.cover_not_available) {
                crossfade(true)
            }
        } else {
            imgBackdrop.load(data.getBackdropUrl()) {
                transformations(BlurTransformation(this@DetailActivity))
                crossfade(true)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        favoriteViewModel.checkFavorite(data.id).observe(this, Observer {
            if (it > 0) menu.findItem(R.id.btnFavorite).icon =
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_24dp)
            else menu.findItem(R.id.btnFavorite).icon =
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnFavorite -> {
                if (isFavorite) {
                    favoriteViewModel.deleteFromFavorite(data.id)
                    showToast("${data.title} ${resources.getString(R.string.delete_favorite)}")
                } else {

                    val favorite = Favorite(
                        null,
                        data.id,
                        data.title,
                        data.releaseDate,
                        data.overview,
                        intent.getStringExtra(
                            EXTRA_KIND
                        ),
                        data.posterPath,
                        data.backdropPath
                    )

                    favoriteViewModel.insert(favorite)
                    showToast("${data.title} ${resources.getString(R.string.added_favorite)}")
                }
            }
        }
        return true
    }

}
