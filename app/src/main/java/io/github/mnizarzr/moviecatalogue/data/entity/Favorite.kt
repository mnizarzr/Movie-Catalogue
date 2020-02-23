package io.github.mnizarzr.moviecatalogue.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tbl_favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val itemId: Int,
    val title: String,
    val releaseDate: String,
    val overview: String,
    val kind: String?,
    val posterPath: String?,
    val backdropPath: String?
) : Parcelable {

    companion object {
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
    }

    fun getPosterUrl(size: String = "w342") = "$BASE_IMAGE_URL$size$posterPath"
    fun getBackdropUrl(size: String = "w500") = "$BASE_IMAGE_URL$size$backdropPath"

}