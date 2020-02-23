package io.github.mnizarzr.moviecatalogue.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.mnizarzr.moviecatalogue.data.model.ApiResponse
import io.github.mnizarzr.moviecatalogue.data.model.ItemResult
import io.github.mnizarzr.moviecatalogue.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val apiService = ApiClient.getService()
    private val listMovies = MutableLiveData<ArrayList<ItemResult>>()

    internal fun setMovies() {
        apiService.getMovies().enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("ERR_GET_MOVIES", t.message ?: "Unknown Error")
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                response.body()?.let {
                    listMovies.postValue(it.results)
                }
            }
        })
    }

    internal fun getMovies(): LiveData<ArrayList<ItemResult>> {
        return listMovies
    }

}