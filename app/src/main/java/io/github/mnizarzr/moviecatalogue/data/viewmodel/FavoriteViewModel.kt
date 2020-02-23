package io.github.mnizarzr.moviecatalogue.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import io.github.mnizarzr.moviecatalogue.data.dao.FavoriteDao
import io.github.mnizarzr.moviecatalogue.data.database.AppDatabase
import io.github.mnizarzr.moviecatalogue.data.entity.Favorite
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteDao: FavoriteDao = AppDatabase.getDatabase(application).favoriteDao()

    private val repository: FavoriteRepository = FavoriteRepository(favoriteDao)

    var allMoviesFavorited: LiveData<List<Favorite>> = repository.allMoviesFavorited
    var allTvFavorited: LiveData<List<Favorite>> = repository.allTvFavorited

    fun insert(favorite: Favorite) = viewModelScope.launch {
        repository.insert(favorite)
    }

    fun checkFavorite(itemId: Int): LiveData<Int> = repository.checkFavorite(itemId)

    fun deleteFromFavorite(itemId: Int) = viewModelScope.launch {
        repository.deleteFromFavorite(itemId)
    }

}