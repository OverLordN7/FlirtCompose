package com.example.flirtcompose.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flirtcompose.navigation.Screen
import com.example.flirtcompose.ui.screens.HomeScreen
import com.example.flirtcompose.ui.screens.ImageBeltScreen
import com.example.flirtcompose.ui.screens.PersonViewModel
import com.example.flirtcompose.ui.screens.ProfileScreen

@Composable
fun FlirtApp(){

    val personViewModel: PersonViewModel = viewModel(factory = PersonViewModel.Factory)
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route){
        composable(route = Screen.HomeScreen.route){
            HomeScreen(
                personViewModel,
                navController,
                personViewModel::getPersonList,
                personViewModel::getFilteredPersonListBySex,
            )
        }

        composable(route = Screen.ProfileScreen.route){
            ProfileScreen(personViewModel = personViewModel,navController)
        }

        composable(
            route = Screen.ImageBeltScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = false
                },
            )
        ){
            ImageBeltScreen(
                personViewModel = personViewModel,
                navController = navController,
                id = it.arguments?.getString("id")!!,
            )
        }
    }
}