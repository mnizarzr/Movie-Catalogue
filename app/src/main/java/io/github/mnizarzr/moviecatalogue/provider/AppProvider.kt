package io.github.mnizarzr.moviecatalogue.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import io.github.mnizarzr.moviecatalogue.data.database.AppDatabase

class AppProvider : ContentProvider() {

    override fun insert(uri: Uri, values: ContentValues?): Uri? = uri

    // need this only
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return context?.let { AppDatabase.getDatabase(it).favoriteDao().getAllCursor() }
    }

    override fun onCreate(): Boolean = true

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? = null

}