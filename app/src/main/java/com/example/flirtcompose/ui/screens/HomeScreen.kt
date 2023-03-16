package com.example.flirtcompose.ui.screens


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.flirtcompose.model.Person
import com.example.flirtcompose.navigation.Screen
import kotlinx.coroutines.flow.Flow


private const val TAG = "HomeScreen"
private const val HEADER = "http://dating.mts.by"

@Composable
fun HomeScreen(
    personViewModel: PersonViewModel,
    navController: NavController,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){

    val state: PersonUiState = personViewModel.personUiState

    when(state){
        is PersonUiState.Loading -> LoadingItem()
        is PersonUiState.Error -> ErrorScreen(retryAction)
        is PersonUiState.Success -> ResultScreen(state.personList,personViewModel,navController,retryAction)
    }
}

@Composable
fun LoadingItem(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .padding(8.dp),
            strokeWidth = 5.dp
        )
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit ,modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Text(text = "Something went wrong. Try Again:(", color = Color.White)
            Button(onClick = retryAction) {
                Text(text = "Again", color = Color.White)
            }
        }
    }
}

@Composable
fun PersonCard(person: Person, navController: NavController, personViewModel: PersonViewModel
){
    val imageURL = HEADER + person.iurl_600

    Card(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(4.dp, Color.Black, RoundedCornerShape(8))
            .clickable {
                personViewModel.person = person

                navController.navigate(Screen.ProfileScreen.route)
            },
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

            //TODO DELETE this line after debug
            Text(person.photos.size.toString())

        }
    }
}

@Composable
fun ResultScreen(
    flowList: Flow<PagingData<Person>>,
    personViewModel: PersonViewModel,
    navController: NavController,
    retryAction: () -> Unit
){
    val personList = flowList.collectAsLazyPagingItems()

    when(personList.loadState.append){
        is LoadState.NotLoading -> Unit
        LoadState.Loading -> { LoadingItem() }
        is LoadState.Error -> { Text(text = "Error3!!", color = Color.White) }
    }

    when(personList.loadState.refresh){
        is LoadState.NotLoading -> Unit
        LoadState.Loading -> { LoadingItem() }
        is LoadState.Error -> { ErrorScreen(retryAction) }
    }

    LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)){
        items(personList.itemCount){
            personList[it]?.let { it1 -> PersonCard(it1,navController,personViewModel) }
        }


//        when(personList.loadState.append){
//            is LoadState.NotLoading -> Unit
//            LoadState.Loading -> {
//                item {
//                    LoadingItem()
//                }
//            }
//            is LoadState.Error -> {
//                item {
//                    Text(text = "Error1!!", color = Color.White)
//                }
//            }
//        }
//
//        when(personList.loadState.refresh){
//            is LoadState.NotLoading -> Unit
//            LoadState.Loading -> {
//                item {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center,
//                        content = {
//                            CircularProgressIndicator()
//                        }
//                    )
//                }
//            }
//            is LoadState.Error ->
//                item{
//                    Text(text = "Error2!!", color = Color.White)
//                }
//        }
    }
}