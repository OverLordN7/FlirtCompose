package com.example.flirtcompose.navigation

sealed class Screen(val route: String){

    object HomeScreen: Screen("home_screen")

    object ProfileScreen: Screen("profile_screen")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg->
                append("/$arg")
            }
        }
    }
}
