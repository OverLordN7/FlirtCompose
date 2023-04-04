package com.example.flirtcompose.ui.screens

import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flirtcompose.R
import com.example.flirtcompose.model.MenuItem
import com.example.flirtcompose.navigation.Screen
import com.example.flirtcompose.ui.custom.PieChart
import com.example.flirtcompose.ui.menu.DefaultAppBar
import com.example.flirtcompose.ui.menu.DrawerBody
import com.example.flirtcompose.ui.menu.DrawerHeader
import kotlinx.coroutines.launch


private const val TAG = "GraphScreen"


@Composable
fun GraphScreen(
    personViewModel: PersonViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Log.d(TAG,"graphlist: ${personViewModel.personListGraph.size}")

    var ageCategory1 = 0 //20..29
    var ageCategory2 = 0 //30..39
    var ageCategory3 = 0 //40..49
    var ageCategory4 = 0 //50..59
    var ageCategory5 = 0 //60..99




    personViewModel.personListGraph.forEach {person ->
        val age = personViewModel.convertStringAgeToInt(person.age)
        when(age){
            in 20..29->{ageCategory1++}
            in 30..39->{ageCategory2++}
            in 40..49->{ageCategory3++}
            in 50..59->{ageCategory4++}
            in 60..99->{ageCategory5++}
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        topBar = {
            DefaultAppBar(
                onNavigationIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            DrawerHeader()
            DrawerBody(items = listOf(
                MenuItem(
                    id = "home",
                    title = stringResource(R.string.menu_item_home),
                    contentDescription = "Go to home screen",
                    icon = Icons.Default.Home
                ),
                MenuItem(
                    id = "settings",
                    title = stringResource(R.string.menu_item_settings),
                    contentDescription = "Go to settings screen",
                    icon = Icons.Default.Settings
                ),
                MenuItem(
                    id = "graphs",
                    title = stringResource(R.string.menu_item_graphs),
                    contentDescription = "Go to graph screen",
                    icon = Icons.Default.AddCircle
                ),
                MenuItem(
                    id = "help",
                    title = stringResource(R.string.menu_item_help),
                    contentDescription = "Get Help",
                    icon = Icons.Default.Info
                ),
            ),
                onItemClick = {
                    when (it.id) {
                        "home" -> {
                            navController.popBackStack(Screen.HomeScreen.route,false)
                        }
                        "graphs" -> {
                            Toast.makeText(context,R.string.menu_item_toast_message,Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val message = context.getString(R.string.menu_item_toast_default,it.title)
                            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    ) {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            LazyColumn {
                item {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp))
                }
                item{
                    PieChart(
                        data = mapOf(
                            Pair("From 20 to 29", ageCategory1),
                            Pair("From 30 to 39", ageCategory2),
                            Pair("From 40 to 49", ageCategory3),
                            Pair("From 50 to 59", ageCategory4),
                            Pair("From 60 to 99", ageCategory5),
                        )
                    )
                }
            }
        }
    }
}