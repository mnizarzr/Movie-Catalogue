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
import io.github.mnizarzr.moviecatalogue.data.viewmodel.FavoriteViewModel
import io.github.mnizarzr.moviecatalogue.utils.showToast
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.SimpleDateFormat
import java.util.*

class DetailFavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var data: Favorite
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

        val data = intent.getParcelableExtra(EXTRA_DATA) as Favorite
        this.data = data

        favoriteViewModel.checkFavorite(data.itemId).observe(this, Observer {
            isFavorite = it > 0
        })

        if (data.releaseDate.isNotBlank()) {
            val date = SimpleDateFormat(DATE, Locale.getDefault()).parse(data.releaseDate)
            val textDate =
                date?.let { SimpleDateFormat(FORMATTED_DATE, Locale.getDefault()).format(it) }
            txtReleaseDate.text = textDate
        }

        val overview: String =
            if (data.overview.isNotEmpty()) data.overview else resources.getString(R.string.text_no_overview)
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

        if (data.backdropPath == null || data.backdropPath.isBlank()) {
            imgBackdrop.load(R.drawable.cover_not_available) {
                crossfade(true)
            }
        } else {
            imgBackdrop.load(data.getBackdropUrl()) {
                transformations(BlurTransformation(this@DetailFavoriteActivity))
                crossfade(true)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        favoriteViewModel.checkFavorite(data.itemId).observe(this, Observer {
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
                    favoriteViewModel.deleteFromFavorite(data.itemId)
                    showToast("${data.title} ${resources.getString(R.string.delete_favorite)}")
                } else {

                    val favorite = Favorite(
                        null,
                        data.itemId,
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
