package com.example.flirtcompose.ui.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.flirtcompose.R
import com.example.flirtcompose.navigation.Screen
import com.example.flirtcompose.ui.theme.TransparentBlack

@Composable
fun ImageBeltScreen(
    personViewModel: PersonViewModel,
    navController: NavController,
    id:String,
    modifier: Modifier = Modifier
){

    var index by remember { mutableStateOf(id.toInt()) }

    val list = personViewModel.photoList


    Card(
        modifier = modifier.fillMaxSize().padding(8.dp),
        backgroundColor = TransparentBlack
    ) {
        Column() {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.weight(0.5f))
                Spacer(modifier = Modifier.weight(8f))
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    border = BorderStroke(0.dp,Color.Transparent),
                    modifier = Modifier
                        .size(50.dp).weight(0.5f),
                    contentPadding = PaddingValues(),
                    onClick = {navController.popBackStack()},
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        contentDescription = "chat",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (index > 0){
                    OutlinedButton(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        border = BorderStroke(0.dp,Color.Transparent),
                        modifier = Modifier
                            .size(25.dp)
                            .weight(0.5f),
                        contentPadding = PaddingValues(),
                        onClick = { index-=1},
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_arrow_left),
                            contentDescription = "chat",
                        )
                    }
                } else{
                    Spacer(modifier = Modifier.weight(0.5f))
                }
                AsyncImage(
                    model = list[index],
                    contentDescription = "image",
                    modifier = Modifier.weight(3f)
                )

                if (index < list.size-1){
                    OutlinedButton(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        border = BorderStroke(0.dp,Color.Transparent),
                        modifier = Modifier
                            .size(25.dp)
                            .weight(0.5f),
                        contentPadding = PaddingValues(),
                        onClick = { index +=1},
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_arrow_right),
                            contentDescription = "chat",
                        )
                    }
                }else{
                    Spacer(modifier = Modifier.weight(0.5f))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}