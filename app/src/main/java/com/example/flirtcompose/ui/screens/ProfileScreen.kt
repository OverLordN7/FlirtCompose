package com.example.flirtcompose.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(
    personName: String,
    personImg: String,

    modifier: Modifier = Modifier
){
    val preparedImg = personImg.replace('^','/')



    Column(modifier = modifier.fillMaxSize()) {
        Text(personName, color = Color.White)
        AsyncImage(model = preparedImg, contentDescription = personName)
    }
}