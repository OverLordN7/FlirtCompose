package com.example.flirtcompose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

        composable(
            route = Screen.ProfileScreen.route+"/{person_name}/{person_img}",
            arguments = listOf(
                navArgument("person_name"){
                    type = NavType.StringType
                    defaultValue = "Tali"
                    nullable = true
                },
                navArgument("person_img"){
                    type = NavType.StringType
                    defaultValue = "Tali"
                    nullable = true
                },
            )
        ){ entry ->
            //entry.arguments?.getString("person_name")?.let { ProfileScreen(personName = it) }
            ProfileScreen(
                personName = entry.arguments?.getString("person_name")!!,
                personImg = entry.arguments?.getString("person_img")!!,
            )
        }
    }
}