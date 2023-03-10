package com.example.flirtcompose.data

import com.example.flirtcompose.network.RequestApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

interface AppContainer{
    val requestRepository: RequestRepository
}

class DefaultAppContainer: AppContainer{
    var BASE_URL = "http://dating.mts.by/android/search/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: RequestApiService by lazy {
        retrofit.create(RequestApiService::class.java)
    }

    override val requestRepository: RequestRepository by lazy {
        NetworkRequestRepository(retrofitService)
    }
}