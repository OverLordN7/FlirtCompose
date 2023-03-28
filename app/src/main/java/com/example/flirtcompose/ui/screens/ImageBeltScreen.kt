package com.example.flirtcompose.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.flirtcompose.R
import com.example.flirtcompose.ui.theme.TransparentBlack

@Composable
fun ImageBeltScreen(
    personViewModel: PersonViewModel,
    navController: NavController,
    id:String,
    modifier: Modifier = Modifier
){
    val index = remember { mutableStateOf(id.toInt()) }
    val list = personViewModel.photoList

    Card(
        backgroundColor = TransparentBlack,
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.weight(9f))

                CustomIconButton(
                    imageRes = R.drawable.baseline_close_24,
                    imageResDesc = R.string.close_button,
                    size = 50,
                    onClick = {navController.popBackStack()}
                )
            }

            ImageItem(index = index, list = list)

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
@Composable
fun ImageItem(
    index: MutableState<Int>,
    list: List<String>,
    modifier: Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (index.value > 0){
            CustomIconButton(
                imageRes = R.drawable.baseline_arrow_left,
                imageResDesc = R.string.previous_button,
                onClick = { index.value-=1 },
                modifier = modifier.weight(0.5f)
            )
        }
        else{ Spacer(modifier = modifier.weight(0.5f)) }

        AsyncImage(
            model = list[index.value],
            contentDescription = stringResource(id = R.string.current_image),
            modifier = modifier.weight(8f),
            contentScale = ContentScale.Fit
        )

        if (index.value < list.size-1){
            CustomIconButton(
                imageRes = R.drawable.baseline_arrow_right,
                imageResDesc = R.string.next_button,
                onClick = {index.value +=1},
                modifier = modifier.weight(0.5f)
            )
        }
        else{ Spacer(modifier = Modifier.weight(0.5f)) }
    }
}