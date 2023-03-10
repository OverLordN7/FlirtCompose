package com.example.flirtcompose.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flirtcompose.RequestApplication
import com.example.flirtcompose.data.RequestRepository

private const val TAG = "PersonViewModel"

class PersonViewModel(
    private val requestRepository: RequestRepository):ViewModel(){

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