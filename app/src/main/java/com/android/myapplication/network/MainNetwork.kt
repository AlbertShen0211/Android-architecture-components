package com.android.myapplication.network

import androidx.lifecycle.LiveData
import com.android.myapplication.data.Banner
import com.android.myapplication.data.Response
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(NetworkInterceptor())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://www.wanandroid.com")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface MainNetwork {
    @GET("next_title.json")
    suspend fun fetchNextTitle(): String

    @GET("/banner/json")
    suspend fun getBanner(): Response<List<Banner>>
}

object Api {
    val retrofitService: MainNetwork by lazy { retrofit.create(MainNetwork::class.java) }
}

