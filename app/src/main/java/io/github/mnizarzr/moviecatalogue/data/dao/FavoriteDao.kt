package io.github.mnizarzr.moviecatalogue.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.mnizarzr.moviecatalogue.data.entity.Favorite

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM tbl_favorite WHERE kind = 'movie'")
    fun getAllFavoriteMovies(): LiveData<List<Favorite>>

    @Query("SELECT * FROM tbl_favorite WHERE kind = 'tvShow'")
    fun getAllFavoriteTvShows(): LiveData<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Query("DELETE FROM tbl_favorite WHERE itemId = :itemId")
    suspend fun deleteItem(itemId: Int)

    @Query("SELECT COUNT(*) FROM tbl_favorite WHERE itemId = :itemId LIMIT 1")
    fun checkFavorite(itemId: Int): LiveData<Int>

}