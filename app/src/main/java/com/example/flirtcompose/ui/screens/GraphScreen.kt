package com.example.flirtcompose.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flirtcompose.R
import com.example.flirtcompose.model.MenuItem
import com.example.flirtcompose.navigation.Screen
import com.example.flirtcompose.ui.custom.PieChart
import com.example.flirtcompose.ui.menu.AppBar
import com.example.flirtcompose.ui.menu.DefaultAppBar
import com.example.flirtcompose.ui.menu.DrawerBody
import com.example.flirtcompose.ui.menu.DrawerHeader
import kotlinx.coroutines.launch

@Composable
fun GraphScreen(
    personViewModel: PersonViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
                    if (it.id == "home"){
                        Toast.makeText(context,"You pressed on ${it.title} redirecting...",Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }else{
                        Toast.makeText(context,"You pressed on ${it.title}",Toast.LENGTH_SHORT).show()
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
            Column {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp))
                PieChart(
                    data = mapOf(
                        Pair("Sample-1", 150),
                        Pair("Sample-2", 120),
                        Pair("Sample-3", 110),
                        Pair("Sample-4", 170),
                        Pair("Sample-5", 120),
                    )
                )
            }
        }
    }
}