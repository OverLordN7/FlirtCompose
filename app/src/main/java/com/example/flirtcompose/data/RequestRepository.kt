package com.example.flirtcompose.data

import com.example.flirtcompose.model.Request
import com.example.flirtcompose.network.RequestApiService
import kotlinx.coroutines.delay
import retrofit2.Response

interface RequestRepository{
    suspend fun getJournals(page: Int): Response<Request>
}

class NetworkRequestRepository(
    private val requestApiService: RequestApiService
): RequestRepository {

    override suspend fun getJournals(page: Int): Response<Request> {
        delay(1500L)
        return requestApiService.getJournals(page)
    }
}