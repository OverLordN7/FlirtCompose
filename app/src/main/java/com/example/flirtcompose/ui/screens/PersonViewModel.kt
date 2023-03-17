package com.example.flirtcompose.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.*
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.flirtcompose.RequestApplication
import com.example.flirtcompose.data.PersonPagingSource
import com.example.flirtcompose.data.RequestRepository
import com.example.flirtcompose.model.Person
import com.example.flirtcompose.model.PersonPhoto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "PersonViewModel"

sealed interface PersonUiState{
    data class Success(var personList: Flow<PagingData<Person>>): PersonUiState
    object Error: PersonUiState

    object Loading: PersonUiState
}

class PersonViewModel(private val requestRepository: RequestRepository):ViewModel(){

    var personUiState : PersonUiState by mutableStateOf(PersonUiState.Loading)
        private set



    var person = Person()

    var photoList: List<String> = emptyList()

    val personPager = Pager(
        PagingConfig(pageSize = 10)
    ){
        PersonPagingSource(requestRepository)
    }.flow.cachedIn(viewModelScope)

    init {
        getPersonList()
    }


    fun getPersonList(){
        viewModelScope.launch {
            personUiState = PersonUiState.Loading
            personUiState = try{
                PersonUiState.Success(personPager.cachedIn(viewModelScope))
            }catch (e: IOException){
                PersonUiState.Error
            } catch (e: HttpException){
                PersonUiState.Error
            }
        }
    }

    fun getFilteredPersonListBySex(sex: Int = 0){
        val filterPager = Pager(
            PagingConfig(pageSize = 10)
        ){
            PersonPagingSource(requestRepository)
        }.flow.map {pagingData->
            pagingData.filter { person->
                person.sex == sex
            }

        }

        viewModelScope.launch {
            personUiState = PersonUiState.Loading
            personUiState = try{
                PersonUiState.Success(filterPager.cachedIn(viewModelScope))
            }catch (e: IOException){
                PersonUiState.Error
            } catch (e: HttpException){
                PersonUiState.Error
            }
        }
    }




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