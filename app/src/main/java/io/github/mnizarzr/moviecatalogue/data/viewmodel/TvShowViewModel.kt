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

class TvShowViewModel : ViewModel() {

    private val apiService = ApiClient.getService()
    private val listTvShow = MutableLiveData<ArrayList<ItemResult>>()

    internal fun setTvShows() {
        apiService.getTvShows().enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("ERR_GET_MOVIES", t.message ?: "Unknown Error")
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                response.body()?.let {
                    listTvShow.postValue(it.results)
                }
            }
        })
    }

    internal fun getTvShows(): LiveData<ArrayList<ItemResult>> {
        return listTvShow
    }

    internal fun searchTvShows(query: String) : LiveData<ArrayList<ItemResult>> {
        val tvShows= MutableLiveData<ArrayList<ItemResult>>()
        apiService.searchTvShow(query).enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("ERR_GET_MOVIES", t.message ?: "Unknown Error")
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                response.body()?.let {
                    tvShows.postValue(it.results)
                }
            }
        })
        return tvShows
    }

}