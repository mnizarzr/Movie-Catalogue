package io.github.mnizarzr.moviecatalogue.data.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.mnizarzr.moviecatalogue.data.entity.Favorite
import io.github.mnizarzr.moviecatalogue.utils.Constants.KIND_MOVIE
import io.github.mnizarzr.moviecatalogue.utils.Constants.KIND_TVSHOW

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite WHERE kind = '$KIND_MOVIE'")
    fun getAllFavoriteMovies(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite WHERE kind = '$KIND_TVSHOW'")
    fun getAllFavoriteTvShows(): LiveData<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE itemId = :itemId")
    suspend fun deleteItem(itemId: Int)

    @Query("SELECT COUNT(*) FROM favorite WHERE itemId = :itemId LIMIT 1")
    fun checkFavorite(itemId: Int): LiveData<Int>

    @Query("SELECT * FROM favorite")
    fun getAll(): List<Favorite>

    @Query("SELECT * FROM favorite")
    fun getAllCursor(): Cursor

}