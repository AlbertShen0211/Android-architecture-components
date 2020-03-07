package com.android.myapplication.network

import androidx.lifecycle.LiveData
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(SkipNetworkInterceptor())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("http://localhost/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface MainNetwork {
    @GET("next_title.json")
    suspend fun fetchNextTitle(): String
}

object Api{
    val retrofitService: MainNetwork by lazy { retrofit.create(MainNetwork::class.java) }

}

