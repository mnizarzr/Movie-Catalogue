package io.github.mnizarzr.moviecatalogue.service

import com.google.gson.*
import io.github.mnizarzr.moviecatalogue.BuildConfig
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object ApiClient {

    private var client: Retrofit? = null

    private const val BASE_URL = BuildConfig.API_URL
    private const val API_KEY = BuildConfig.API_KEY

    private val language: String
        get() {
            var locale = Locale.getDefault().language
            val country = Locale.getDefault().country

            if (locale == "in") locale = "id"

            return "${locale}-${country}"
        }

    private val gson: Gson
        get() {
            return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        }

    private val okHttpClient: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest: Request = chain.request()
                    val originalHttpUrl: HttpUrl = originalRequest.url()
                    val newUrl: HttpUrl = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .addQueryParameter("language", language)
                        .build()

                    val request: Request = originalRequest.newBuilder().url(newUrl).build()

                    chain.proceed(request)
                }
                .build()
        }

    private fun getRetrofitInstance(): Retrofit {
        if (client == null) {
            client = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return client!!
    }

    fun getService(): ApiService = getRetrofitInstance().create(ApiService::class.java)

}