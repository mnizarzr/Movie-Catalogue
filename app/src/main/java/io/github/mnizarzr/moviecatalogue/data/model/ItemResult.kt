package io.github.mnizarzr.moviecatalogue.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemResult(
    val adult: Boolean = false,
    val backdropPath: String? = "",
    val genreIds: ArrayList<Int> = arrayListOf(),
    val id: Int = 0,
    val originalLanguage: String = "",
    @SerializedName(value = "original_title", alternate = ["original_name"])
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String? = "",
    @SerializedName(value = "release_date", alternate = ["first_air_date"])
    val releaseDate: String = "",
    @SerializedName(value = "title", alternate = ["name"])
    val title: String = "",
    val video: Boolean = false,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0
) : Parcelable {

    companion object {
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
    }

    fun getPosterUrl(size: String = "w342") = "$BASE_IMAGE_URL$size$posterPath"
    fun getBackdropUrl(size: String = "w500") = "$BASE_IMAGE_URL$size$backdropPath"
    
}