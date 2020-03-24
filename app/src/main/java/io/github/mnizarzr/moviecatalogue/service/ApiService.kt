package io.github.mnizarzr.moviecatalogue.service

import io.github.mnizarzr.moviecatalogue.data.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    fun getMovies(): Call<ApiResponse>

    @GET("tv/popular")
    fun getTvShows(): Call<ApiResponse>

    @GET("search/movie")
    fun searchMovie(@Query("query") query: String): Call<ApiResponse>

    @GET("search/tv")
    fun searchTvShow(@Query("query") query: String): Call<ApiResponse>

    @GET("discover/movie")
    fun getTodayRelease(@Query("primary_release_date.gte") from: String, @Query("primary_release_date.lte") to: String): Call<ApiResponse>

}