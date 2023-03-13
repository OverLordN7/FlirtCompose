package com.example.flirtcompose.data


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.flirtcompose.model.Person

class PersonPagingSource(
    private val requestRepository: RequestRepository
    ): PagingSource<Int, Person>() {
    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        return try {
            val page =params.key ?: 1
            val response = requestRepository.getJournals(page)
            LoadResult.Page(
                data = response.body()!!.users,
                prevKey = null,
                nextKey = if (response.body()!!.users.isNotEmpty()) page + 1 else null

            )

        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}