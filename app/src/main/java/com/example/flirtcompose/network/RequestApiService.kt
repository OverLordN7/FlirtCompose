package com.example.flirtcompose.network

import com.example.flirtcompose.model.Request
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestApiService {

    @GET("ByLiked")
    suspend fun getJournals(
        @Query("page") page:Int,
    ): Response<Request>


}