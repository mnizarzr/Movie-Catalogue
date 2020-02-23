package io.github.mnizarzr.moviecatalogue.service

import io.github.mnizarzr.moviecatalogue.data.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("movie/popular")
    fun getMovies() : Call<ApiResponse>

    @GET("tv/popular")
    fun getTvShows() : Call<ApiResponse>
}