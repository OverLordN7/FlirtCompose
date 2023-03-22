package com.example.flirtcompose.ui.screens


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.flirtcompose.R
import com.example.flirtcompose.model.Person
import com.example.flirtcompose.navigation.Screen
import com.example.flirtcompose.ui.theme.Crimson100
import com.example.flirtcompose.ui.theme.Grey100
import com.example.flirtcompose.ui.theme.Purple200
import kotlinx.coroutines.flow.Flow


private const val TAG = "HomeScreen"
private const val HEADER = "http://dating.mts.by"

@Composable
fun HomeScreen(
    personViewModel: PersonViewModel,
    navController: NavController,
    retryAction: () -> Unit,
    filterAction: (sex: Int,ageStartPosition: Int,ageEndPosition: Int) -> Unit,
    modifier: Modifier = Modifier
){
    val showDialog = remember { mutableStateOf(false) }
    var menuState by remember { mutableStateOf(false) }

    val state: PersonUiState = personViewModel.personUiState

    if (showDialog.value){
        FilterDialog(
            setShowDialog = {showDialog.value = it},
            filterAction = filterAction,
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {

                    IconButton(onClick = retryAction) {
                        Icon(Icons.Default.Refresh,"Return to Default List")
                    }

                    IconButton( onClick = { menuState = !menuState } ) {
                        Icon(Icons.Default.MoreVert,"Menu")
                    }

                    DropdownMenu( expanded = menuState, onDismissRequest = { menuState = false} ) {
                        DropdownMenuItem(
                            onClick = {
                                showDialog.value = true
                                menuState = false
                            } ) {
                            Text(text = "Filter")
                        }
                    }
                }
            )
        },
    ) {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            when(state){
                is PersonUiState.Loading -> LoadingScreen()
                is PersonUiState.Error -> ErrorScreen(retryAction)
                is PersonUiState.Success -> ResultScreen(
                    flowList = state.personList,
                    personViewModel = personViewModel,
                    navController = navController,
                    retryAction = retryAction
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator(
            strokeWidth = 5.dp,
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
        )
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit ,modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Column {
            Text(text = "Something went wrong. Try Again:(", color = Color.White)
            Button(onClick = retryAction) {
                Text(text = "Again", color = Color.White)
            }
        }
    }
}

@Composable
fun PersonCard(
    person: Person,
    navController: NavController,
    personViewModel: PersonViewModel
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                model = imageURL,
                contentDescription = "image",
                modifier = Modifier.padding(4.dp)
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
        LoadState.Loading -> { LoadingScreen() }
        is LoadState.Error -> { ErrorScreen(retryAction) }
    }

    when(personList.loadState.refresh){
        is LoadState.NotLoading -> Unit
        LoadState.Loading -> { LoadingScreen() }
        is LoadState.Error -> { ErrorScreen(retryAction) }
    }

    LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)){
        items(personList.itemCount){ index ->
            personList[index]?.let { person ->
                PersonCard(
                    person = person,
                    navController = navController,
                    personViewModel = personViewModel,
                )
            }
        }
    }
}

@Composable
fun FilterDialog(setShowDialog: (Boolean) -> Unit, filterAction: (sex: Int,ageStartPosition: Int,ageEndPosition: Int) -> Unit){

    val isSexSelected = remember { mutableStateOf(2) }
    val startPosition = remember { mutableStateOf(0) }
    val endPosition = remember { mutableStateOf(100) }

    Dialog(
        onDismissRequest = { setShowDialog(false) }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .size(600.dp)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Filter",
                    color = Color.Black,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.weight(1f))

                CustomSlider(
                    startPosition,
                    endPosition,
                    modifier = Modifier.weight(2f)
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .padding(4.dp)
                ) {
                    Text("Select gender: ", modifier = Modifier.padding(bottom = 4.dp))
                    CustomSwitcher(isSexSelected)
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Button(
                        onClick = {
                            setShowDialog(false)
                            filterAction(
                                isSexSelected.value,
                                startPosition.value,
                                endPosition.value
                            )
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun CustomSwitcher(sex: MutableState<Int>, modifier: Modifier = Modifier){

    val states = listOf("Female","Both", "Male")
    var selectedState by remember { mutableStateOf(states[1])}
    val onStateChange = { text: String -> selectedState = text }

    sex.value = when(selectedState){
        states[0] -> 0
        states[2] -> 1
        else -> 2
    }

    Surface(
        shape = RoundedCornerShape(24.dp),
        elevation = 4.dp,
        modifier = modifier
            .wrapContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(24.dp))
                .background(Grey100)
        ) {
            states.forEach { text->
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = text,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(24.dp))
                            .clickable { onStateChange(text) }
                            .background(if (text == selectedState) Crimson100 else Grey100)
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                            .size(width = 40.dp, height = 20.dp),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomSlider(
    startPosition: MutableState<Int>,
    endPosition: MutableState<Int>,
    modifier: Modifier = Modifier
){
    var sliderStatus by remember { mutableStateOf(20f..100f) }
    var start by remember { mutableStateOf(20) }
    var end by remember { mutableStateOf(100) }

    Card(
        elevation = 4.dp,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Select preferable age: ")
            RangeSlider(
                values = sliderStatus,
                onValueChange = {sliderStatus_ ->
                    sliderStatus = sliderStatus_
                },
                valueRange = 20f..100f,
                onValueChangeFinished = {
                    Log.d(TAG, "Start: ${sliderStatus.start}, End: ${sliderStatus.endInclusive}")

                    startPosition.value = sliderStatus.start.toInt()
                    endPosition.value = sliderStatus.endInclusive.toInt()

                },
                steps = 0,
                modifier = Modifier.padding(4.dp)
            )

            start= sliderStatus.start.toInt()
            end = sliderStatus.endInclusive.toInt()

            Text(text = "Start: ${sliderStatus.start.toInt()}, End: ${sliderStatus.endInclusive.toInt()}")

        }

    }
}