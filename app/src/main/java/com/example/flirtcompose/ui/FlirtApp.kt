package com.example.flirtcompose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flirtcompose.R
import com.example.flirtcompose.ui.screens.PersonViewModel

@Composable
fun FlirtApp(modifier: Modifier = Modifier){
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(stringResource(id = R.string.app_name))})}
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {

            val personViewModel: PersonViewModel = viewModel(factory = PersonViewModel.Factory)

        }
    }
}