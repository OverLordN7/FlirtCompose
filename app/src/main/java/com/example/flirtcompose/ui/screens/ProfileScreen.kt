package com.example.flirtcompose.ui.screens


import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.flirtcompose.R
import com.example.flirtcompose.model.Person
import com.example.flirtcompose.navigation.Screen
import com.example.flirtcompose.ui.theme.*


private const val HEADER = "http://dating.mts.by"


@Composable
fun ProfileScreen(
    personViewModel: PersonViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val person = personViewModel.person

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar( title = { Text(text = stringResource(id = R.string.app_name)) })
        },
    ) {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    PersonalInfoCard(
                        person = person,
                        onBack = {navController.popBackStack()}
                    )
                }
                item {
                    ButtonMenu()
                }
                item {
                    GalleryCard(
                        person = person,
                        personViewModel = personViewModel,
                        onPhotoSelected = { id->
                            navController
                                .navigate( Screen.ImageBeltScreen.withArgs(id.toString()))
                        }
                    )
                }
                item {
                    Text(text = person.greeting, color = Color.White)
                }
            }
        }
    }
}


@Composable
fun PersonalInfoCard(person: Person, onBack:()->Unit){

    val preparedImg = HEADER + person.iurl_600
    val context = LocalContext.current

    //Rainbow border for image setup
    val borderWidth = 4.dp
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

    Row(modifier = Modifier.fillMaxWidth() ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.weight(1f)
        ) {
            CustomIconButton(
                imageRes = R.drawable.baseline_arrow_back_24,
                imageResDesc = R.string.back_arrow_button,
                onClick = onBack,
                size = 50,
                modifier = Modifier.weight(0.5f)
            )
        }

        Card(
            backgroundColor = DarkGray,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .padding(4.dp),
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Row {
                    AsyncImage(
                        model = preparedImg,
                        contentDescription = person.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .border(borderWidth, rainbowColorBrush, CircleShape)
                            .padding(borderWidth)
                            .clip(CircleShape)
                            .size(150.dp),
                    )
                }

                Row {
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
                        CustomIconButton(
                            imageRes = R.drawable.baseline_location_on_24,
                            imageResDesc = R.string.location,
                            onClick = {
                                Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_SHORT)
                                .show()
                            },
                            size = 25
                        )

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
fun GalleryCard(
    person: Person,
    personViewModel: PersonViewModel,
    onPhotoSelected: (Int)->Unit
){

    var isExpanded by remember { mutableStateOf(false) }
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
                .padding(8.dp)
        ) {

            Text(
                text = stringResource(id = R.string.gallery),
                color = Color.White
            )

            Box(
                modifier = Modifier.height(expandHeightGrid.dp)
            ){
                LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)){

                    items(photoList.size){
                        PhotoCard(
                            image = photoList[it],
                            onClick = {onPhotoSelected(it)}
                        )
                    }
                }
            }

            //If person contains more than 3 images, add option to expand gallery
            if (photoList.size>3){
                CustomIconButtonTwoStates(
                    imageRes1 = R.drawable.baseline_expand_less_24,
                    imageRes2 = R.drawable.baseline_expand_more_24,
                    imageResDesc = R.string.expand_gallery,
                    status = isExpanded ,
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
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotoCard(image: String, onClick: () -> Unit){
    Card(onClick = onClick) {
        AsyncImage(model = image, contentDescription = stringResource(id = R.string.select_photo))
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .height(80.dp)
            .padding(8.dp)
            .fillMaxWidth(),
    ){

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ){
            CustomIconButtonTwoStates(
                imageRes1 = R.drawable.baseline_favorite_24,
                imageRes2 = R.drawable.baseline_favorite_border_24,
                imageResDesc = R.string.add_to_favorite,
                status = isFavorite ,
                onClick = { isFavorite = !isFavorite })

            Text(stringResource(id = R.string.favorite_button), color = Color.White)
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ){
            CustomIconButton(
                imageRes = R.drawable.baseline_chat_24,
                imageResDesc = R.string.chat_button,
                onClick = {
                    Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_SHORT)
                        .show()
                },
            )
            Text(stringResource(id = R.string.chat_button), color = Color.White)
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            CustomIconButtonTwoStates(
                imageRes1 = R.drawable.baseline_thumb_up_24,
                imageRes2 = R.drawable.outline_thumb_up_24,
                imageResDesc = R.string.like_button,
                status = isLiked,
                onClick = {
                    if (!isLiked){
                        countLikes.value +=1
                        isLiked = true
                    } else {
                        countLikes.value -= 1
                        isLiked = false }
                }
            )
            Text(text = countLikes.value.toString(), color = Color.White)
        }
    }
}

@Composable
fun CustomIconButton(
    imageRes: Int,
    imageResDesc: Int,
    onClick: ()->Unit,
    modifier: Modifier = Modifier,
    size: Int = 25,
){
    OutlinedButton(
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        border = BorderStroke(0.dp,Color.Transparent),
        contentPadding = PaddingValues(),
        onClick = onClick,
        modifier = modifier
            .size(size.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = stringResource(id = imageResDesc),
            modifier = Modifier.size(25.dp)
        )
    }
}

@Composable
fun CustomIconButtonTwoStates(
    imageRes1: Int,
    imageRes2: Int,
    imageResDesc: Int,
    status: Boolean,
    onClick: ()->Unit,
    modifier: Modifier = Modifier,
    size: Int = 25,
){
    OutlinedButton(
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        border = BorderStroke(0.dp,Color.Transparent),
        contentPadding = PaddingValues(),
        onClick = onClick,
        modifier = modifier
            .size(size.dp)
    ) {
        Image(
            painter = painterResource(id = if(status) imageRes1 else imageRes2),
            contentDescription = stringResource(id = imageResDesc),
            modifier = Modifier.size(25.dp)
        )
    }
}