package io.github.mnizarzr.moviecatalogue.data.viewmodel

import androidx.lifecycle.LiveData
import io.github.mnizarzr.moviecatalogue.data.dao.FavoriteDao
import io.github.mnizarzr.moviecatalogue.data.entity.Favorite

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    val allMoviesFavorited: LiveData<List<Favorite>> = favoriteDao.getAllFavoriteMovies()
    val allTvFavorited: LiveData<List<Favorite>> = favoriteDao.getAllFavoriteTvShows()

    suspend fun insert(favorite: Favorite) {
        favoriteDao.insert(favorite)
    }

    fun checkFavorite(itemId: Int): LiveData<Int> = favoriteDao.checkFavorite(itemId)

    suspend fun deleteFromFavorite(itemId: Int) {
        favoriteDao.deleteItem(itemId)
    }

}