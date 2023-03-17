package com.example.flirtcompose.ui.screens


import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.flirtcompose.R
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
    filterAction: (sex: Int) -> Unit,
    modifier: Modifier = Modifier
){

    val showDialog = remember { mutableStateOf(false) }

    val state: PersonUiState = personViewModel.personUiState
    var menuState by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDialog.value){
        FilterDialog(setShowDialog = {showDialog.value = it})
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name))},
                actions = {

                    IconButton(onClick = retryAction) {
                        Icon(Icons.Default.Refresh,"Return to Default List")
                    }


                    IconButton(onClick = {menuState = !menuState}) {
                        Icon(Icons.Default.MoreVert,"Menu")
                    }

                    DropdownMenu(
                        expanded = menuState,
                        onDismissRequest = { menuState = false}
                    ) {

                        DropdownMenuItem(onClick = {
                            showDialog.value = true
                            //filterAction(0)
                        }) {
                            Text(text = "Filter")
                        }
                        DropdownMenuItem(onClick = {
                            Toast.makeText(context,"Search",Toast.LENGTH_SHORT).show()

                        }) {
                            Text(text = "Search")
                        }

                    }
                }
            )
        },

        ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            when(state){
                is PersonUiState.Loading -> LoadingItem()
                is PersonUiState.Error -> ErrorScreen(retryAction)
                is PersonUiState.Success -> ResultScreen(state.personList,personViewModel,navController,retryAction)
            }
        }
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
    }
}

@Composable
fun FilterDialog(setShowDialog: (Boolean) -> Unit){
    Dialog(onDismissRequest = {setShowDialog(false)}) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(600.dp)
            ){
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Test")
                    Button(onClick = {
                        setShowDialog(false)
                    }) {
                        Text(text = "Done")
                    }

                }
            }

        }
    }
}