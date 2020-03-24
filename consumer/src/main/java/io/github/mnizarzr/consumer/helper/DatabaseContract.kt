package io.github.mnizarzr.consumer.helper

import android.net.Uri

object DatabaseContract {

    private const val AUTHORITY = "io.github.mnizarzr.moviecatalogue"
    private const val SCHEME = "content"
    private const val TABLE_NAME = "favorite"

    object FavoriteColumns {
        const val TITLE = "title"
        const val OVERVIEW = "overview"
        const val POSTER_PATH = "posterPath"
    }

    val CONTENT_URI: Uri = Uri.Builder()
        .scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(TABLE_NAME)
        .build()

}