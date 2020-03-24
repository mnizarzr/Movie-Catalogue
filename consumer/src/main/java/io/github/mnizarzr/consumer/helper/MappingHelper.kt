package io.github.mnizarzr.consumer.helper

import android.database.Cursor
import io.github.mnizarzr.consumer.entity.Favorite

object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): List<Favorite> {
        val favorites: ArrayList<Favorite> = arrayListOf()
        if (favoriteCursor != null) {
            while (favoriteCursor.moveToNext()) {
                val title =
                    favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TITLE))
                val overview =
                    favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.OVERVIEW))
                val posterPath =
                    favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.POSTER_PATH))
                favorites.add(Favorite(title, overview, posterPath))
            }
        }
        return favorites
    }

}