package com.example.flirtcompose.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.flirtcompose.RequestApplication
import com.example.flirtcompose.data.PersonPagingSource
import com.example.flirtcompose.data.RequestRepository
import com.example.flirtcompose.model.Person
import com.example.flirtcompose.model.PersonPhoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

private const val TAG = "PersonViewModel"

class PersonViewModel(private val requestRepository: RequestRepository):ViewModel(){

    var person = Person()

    var photoList: List<String> = emptyList()

    val personPager = Pager(
        PagingConfig(pageSize = 10)
    ){
        PersonPagingSource(requestRepository)
    }.flow.cachedIn(viewModelScope)





    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RequestApplication)
                val requestRepository = application.container.requestRepository
                PersonViewModel(requestRepository)
            }
        }
    }
}