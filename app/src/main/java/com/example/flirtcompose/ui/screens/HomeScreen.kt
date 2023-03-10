package com.example.flirtcompose.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.flirtcompose.model.Person




@Composable
fun HomeScreen(personViewModel: PersonViewModel,modifier: Modifier = Modifier){
    DisplayListPersons(personViewModel)

}

@Composable
fun DisplayListPersons(personViewModel: PersonViewModel){
    val personlist = personViewModel.personPager.collectAsLazyPagingItems()

    LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)){
        items(personlist.itemCount){
            personlist[it]?.let { it1 -> PersonCard(it1) }
        }

        when(personlist.loadState.append){
            is LoadState.NotLoading -> Unit
            LoadState.Loading -> {
                item {
                    LoadingItem()
                }
            }
            is LoadState.Error -> {
                item {

                }
            }
        }

        when(personlist.loadState.refresh){
            is LoadState.NotLoading -> Unit
            LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                        content = {
                            CircularProgressIndicator()
                        }
                    )
                }
            }
            is LoadState.Error -> TODO()
        }
    }


}

@Composable
fun LoadingItem(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(42.dp)
                .height(42.dp)
                .padding(8.dp),
            strokeWidth = 5.dp
        )
    }
}

@Composable
fun PersonCard(person: Person){
    val imageURL = "http://dating.mts.by"+person.iurl_600
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(4.dp, Color.Black, RoundedCornerShape(8)),
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = imageURL,
                contentDescription = "image",
                modifier = Modifier
                    .padding(4.dp)
            )

        }
    }
}