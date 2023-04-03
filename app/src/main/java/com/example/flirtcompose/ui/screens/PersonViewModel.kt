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
import com.example.flirtcompose.RequestApplication
import com.example.flirtcompose.data.PersonPagingSource
import com.example.flirtcompose.data.RequestRepository
import com.example.flirtcompose.model.Person
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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

    var personListGraph: List<Person> = emptyList()

    private val dataCache = mutableMapOf<String,List<Person>>()
    private val DEFAULT_QUERY = "default_query"
    private val currentQueryValue: String? = null


    init {
        getPersonList()
        getPersonListForGraph()
    }

    private fun getPersonListForGraph(times: Int = 10){
        var index = 1
        viewModelScope.launch {
            while (index < times){
                val response = requestRepository.getJournals(index)

                if (response.body() != null){
                    personListGraph = personListGraph + response.body()!!.users
                }
                delay(1500)
                index++
            }
        }
    }

    fun getPersonList(){

//        val personPager = Pager(
//            PagingConfig(pageSize = 10)
//        ){
//            PersonPagingSource(requestRepository)
//        }.flow.cachedIn(viewModelScope)
//
//        viewModelScope.launch {
//            personUiState = PersonUiState.Loading
//            personUiState = try{
//                PersonUiState.Success(personPager.cachedIn(viewModelScope))
//            }catch (e: IOException){
//                PersonUiState.Error
//            } catch (e: HttpException){
//                PersonUiState.Error
//            }
//        }

        viewModelScope.launch {
            personUiState = PersonUiState.Loading
            personUiState = try{
                PersonUiState.Success(getPersonListFromCacheOrInternet().cachedIn(viewModelScope))
            }catch (e: IOException){
                PersonUiState.Error
            } catch (e: HttpException){
                PersonUiState.Error
            }
        }



    }

    fun getPersonListFromCacheOrInternet():Flow<PagingData<Person>>{
        val lastQuery = currentQueryValue ?: DEFAULT_QUERY
        val cacheKey = lastQuery.hashCode().toString()

        val cacheResult = dataCache[cacheKey]

        if (cacheResult != null){
            return flowOf(PagingData.from(cacheResult))
        }

        val newResult = Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {PersonPagingSource(requestRepository)}
        ).flow.cachedIn(viewModelScope)

        return newResult
    }

    fun getFilteredPersonList(
        sex: Int = 0,
        ageStartPosition: Int = 0,
        ageEndPosition: Int = 100,
        photoStartPosition: Int = 1,
        photoEndPosition: Int = 100,
    ){
        val filterPager = Pager(PagingConfig(pageSize = 10)){
            PersonPagingSource(requestRepository)
        }.flow.map {pagingData->
            pagingData.filter { person->
                filter(
                    person,
                    sex,
                    ageStartPosition,
                    ageEndPosition,
                    photoStartPosition,
                    photoEndPosition
                )
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

    private fun filter(
        person: Person,
        sex: Int,
        ageStartPosition: Int,
        ageEndPosition: Int,
        photoStartPosition: Int,
        photoEndPosition: Int,
    ): Boolean{

        val age = convertStringAgeToInt(person.age)

        Log.d(TAG, "filter person city: ${person.city}")

        return if (sex == 2){
            //neglect this parameter
            (age in ageStartPosition..ageEndPosition)
                    && (person.photos.size in photoStartPosition..photoEndPosition)
        } else{
            person.sex == sex
                    && (age in ageStartPosition..ageEndPosition)
                    && (person.photos.size in photoStartPosition..photoEndPosition)
        }
    }
    fun convertStringAgeToInt(age: String): Int{
        // clean string from spaces ' '
        var rawAge = age.replace(" ","")
        // clean string from letters of russian alphabet
        rawAge = rawAge.replace("[а-я]".toRegex(),"")

        return rawAge.toInt()
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