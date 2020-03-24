package io.github.mnizarzr.consumer

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.github.mnizarzr.consumer.helper.DatabaseContract.CONTENT_URI
import io.github.mnizarzr.consumer.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var favoriteAdapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            title = resources.getString(R.string.app_title)
        }

        favoriteAdapter = DataAdapter(this)
        rvFavorite.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = favoriteAdapter
            hasFixedSize()
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        contentResolver.registerContentObserver(CONTENT_URI, true, object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                loadFavoritesAsync()
            }
        })

        loadFavoritesAsync()

    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorites = deferredNotes.await()
            progressBar.visibility = View.GONE
            if (favorites.isNotEmpty()) {
                favoriteAdapter.setData(favorites)
            } else {
                Snackbar.make(
                    rvFavorite,
                    resources.getString(R.string.text_no_data),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

}
