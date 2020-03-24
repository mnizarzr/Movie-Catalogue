package io.github.mnizarzr.consumer.entity

import android.os.Parcelable
import io.github.mnizarzr.consumer.BuildConfig
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    val title: String,
    val overview: String,
    val posterPath: String?
) : Parcelable {
    fun getPosterUrl(size: String = "w342") = "${BuildConfig.BASE_IMAGE_URL}$size$posterPath"
}