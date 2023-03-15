package com.example.flirtcompose.ui.screens


import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.flirtcompose.ui.theme.*
import com.example.flirtcompose.R
import com.example.flirtcompose.model.Person
import com.example.flirtcompose.navigation.Screen


private const val TAG = "ProfileScreen"
private const val HEADER = "http://dating.mts.by"


@Composable
fun ProfileScreen(personViewModel: PersonViewModel,navController:NavController,modifier: Modifier = Modifier){
    val person = personViewModel.person

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PersonalInfoCard(person = person,navController)
        ButtonMenu()
        GalleryCard(person = person,navController, personViewModel = personViewModel)
        Text(text = person.greeting,color = Color.White)

    }
}


@Composable
fun PersonalInfoCard(person: Person,navController: NavController){
    val preparedImg = HEADER + person.iurl_600
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.weight(1f)
        ) {
            OutlinedButton(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                border = BorderStroke(0.dp,Color.Transparent),
                modifier = Modifier
                    .size(50.dp).weight(0.5f),
                contentPadding = PaddingValues(),
                onClick = {navController.popBackStack()},
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "chat",
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .padding(4.dp),
            backgroundColor = DarkGray,
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Row(){
                    //Main Image
                    //Rainbow border for image setup
                    val rainbowColorBrush = remember {
                        Brush.sweepGradient(
                            listOf(
                                RainbowPurple,
                                RainbowPink,
                                RainbowMango,
                                RainbowOrange,
                                RainbowYellow,
                                RainbowLightGreen,
                                RainbowLightBlue,
                                RainbowLightPurple
                            )
                        )
                    }
                    val borderWidth = 4.dp

                    AsyncImage(
                        model = preparedImg,
                        contentDescription = person.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(150.dp)
                            .border(borderWidth, rainbowColorBrush, CircleShape)
                            .padding(borderWidth)
                            .clip(CircleShape)
                        ,
                    )
                }
                Row(){
                    //Name
                    Text(
                        text = person.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.Center) {
                    //Age and Location
                    Text(
                        text = person.age,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            border = BorderStroke(0.dp,Color.Transparent),
                            modifier = Modifier.size(25.dp),
                            contentPadding = PaddingValues(),
                            onClick = {
                                Toast.makeText(
                                    context, "option under development", Toast.LENGTH_SHORT).show()
                                //TODO switch to the map, and show city.
                            }) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_location_on_24),
                                contentDescription = stringResource(R.string.location),
                            )
                        }
                        Text(
                            text = person.city,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        )
                    }

                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun GalleryCard(person: Person,navController: NavController,personViewModel: PersonViewModel){

    var isExpanded by remember { mutableStateOf(false) }
    var isBigPhotoSet by remember { mutableStateOf(false) }
    var expandHeight by remember { mutableStateOf(200) }
    var expandHeightGrid by remember { mutableStateOf(140) }

    val personPhotoList = person.photos
    val photoList = ArrayList<String>()

    personPhotoList.forEach {
        val item = HEADER + it.url
        photoList.add(item)
    }

    personViewModel.photoList = photoList



    Card(
        backgroundColor = DarkGray,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(4.dp)
            .height(expandHeight.dp)
            .fillMaxWidth(),
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {

            Text(text = "Gallery", color = Color.White)

            Box(modifier = Modifier.height(expandHeightGrid.dp)){
                LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)){
                    items(photoList.size){
                        PhotoCard(photoList[it],navController,it)
                    }
                }
            }
            if (photoList.size>3){
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    border = BorderStroke(0.dp,Color.Transparent),
                    modifier = Modifier.size(25.dp),
                    contentPadding = PaddingValues(),
                    onClick = {
                        if (!isExpanded){
                            expandHeight = 500
                            expandHeightGrid = 400
                            isExpanded = true
                        } else{
                            expandHeight = 200
                            expandHeightGrid = 140
                            isExpanded = false
                        }


                    }) {
                    Image(
                        painter = painterResource(id = if (isExpanded) R.drawable.baseline_expand_less_24
                        else R.drawable.baseline_expand_more_24),
                        contentDescription = "add to favorite",
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoCard(image: String,navController: NavController,id: Int){
    Card(modifier = Modifier.clickable {

        navController.navigate(Screen.ImageBeltScreen.withArgs(id.toString()))
    }) {
        AsyncImage(model = image, contentDescription = "image")
    }
}

@Composable
fun ButtonMenu(){
    val randomLikes = (5..100).random()
    val countLikes = remember { mutableStateOf(randomLikes) }
    
    var isFavorite by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .height(80.dp)
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ){
            OutlinedButton(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                border = BorderStroke(0.dp,Color.Transparent),
                modifier = Modifier.size(25.dp),
                contentPadding = PaddingValues(),
                onClick = {isFavorite = !isFavorite}) {
                Image(
                    painter = painterResource(id = if (isFavorite) R.drawable.baseline_favorite_24
                    else R.drawable.baseline_favorite_border_24),
                    contentDescription = "add to favorite",
                )
            }
            Text("Favorite", color = Color.White)
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ){
            OutlinedButton(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                border = BorderStroke(0.dp,Color.Transparent),
                modifier = Modifier.size(25.dp),
                contentPadding = PaddingValues(),
                onClick = {
                    Toast.makeText(context,"Not implemented", Toast.LENGTH_SHORT).show()
                }) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_chat_24),
                    contentDescription = "chat",
                )
            }
            Text("Chat", color = Color.White)
        }


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            OutlinedButton(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                border = BorderStroke(0.dp,Color.Transparent),
                modifier = Modifier.size(25.dp),
                contentPadding = PaddingValues(),
                onClick = {
                    if (!isLiked){
                        countLikes.value +=1
                        isLiked = true
                    } else {
                        countLikes.value -= 1
                        isLiked = false
                    }

                }) {
                Image(
                    painter = painterResource(id = if(isLiked) R.drawable.baseline_thumb_up_24 else R.drawable.outline_thumb_up_24),
                    contentDescription = "like",
                )
            }
            Text(text = countLikes.value.toString(), color = Color.White)
        }
    }
}