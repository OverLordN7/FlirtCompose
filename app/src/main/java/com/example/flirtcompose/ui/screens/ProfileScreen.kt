package com.example.flirtcompose.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import com.example.flirtcompose.ui.theme.*
import com.example.flirtcompose.R
import com.example.flirtcompose.model.Person


private const val TAG = "ProfileScreen"
private const val HEADER = "http://dating.mts.by"


@Composable
fun ProfileScreen(personViewModel: PersonViewModel,modifier: Modifier = Modifier){
    val person = personViewModel.person

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PersonalInfoCard(person = person)



        Text(text = person.greeting,color = Color.White)
        Text(text = person.iurl_200,color = Color.White)
        Text(text = person.photos.size.toString(),color = Color.White)
        Text(text = person.photos[0].url,color = Color.White)
        Text(text = person.sex.toString(),color = Color.White)

    }
}


@Composable
fun PersonalInfoCard(person: Person){
    val preparedImg = HEADER + person.iurl_600
    val context = LocalContext.current
    
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
}