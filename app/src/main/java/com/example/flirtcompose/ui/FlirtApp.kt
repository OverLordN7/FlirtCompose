package com.example.flirtcompose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flirtcompose.navigation.Screen
import com.example.flirtcompose.ui.screens.HomeScreen
import com.example.flirtcompose.ui.screens.PersonViewModel
import com.example.flirtcompose.ui.screens.ProfileScreen

@Composable
fun FlirtApp(modifier: Modifier = Modifier){

    val personViewModel: PersonViewModel = viewModel(factory = PersonViewModel.Factory)
    //Nav host section
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route){
        composable(route = Screen.HomeScreen.route){
            HomeScreen(personViewModel,navController)
        }

        composable(route = Screen.ProfileScreen.route){
            ProfileScreen(personViewModel = personViewModel)
        }
    }
}