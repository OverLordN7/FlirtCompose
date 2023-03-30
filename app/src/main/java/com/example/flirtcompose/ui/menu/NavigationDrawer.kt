package com.example.flirtcompose.ui.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flirtcompose.R
import com.example.flirtcompose.model.MenuItem

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Header", fontSize = 60.sp)
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(modifier) {
        items(items.size) {item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(items[item])
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = items[item].icon,
                    contentDescription = items[item].contentDescription
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = items[item].title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit,
    retryAction: () -> Unit,
    menuState: MutableState<Boolean>,
    showDialog: MutableState<Boolean>,

) {


    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer"
                )
            }
        },
        actions = {

            IconButton(onClick = retryAction) {
                Icon(Icons.Default.Refresh, stringResource(id = R.string.refresh))
            }

            IconButton( onClick = { menuState.value = !menuState.value } ) {
                Icon(Icons.Default.MoreVert, stringResource(id = R.string.menu))
            }

            DropdownMenu( expanded = menuState.value, onDismissRequest = { menuState.value = false} ) {
                DropdownMenuItem(
                    onClick = {
                        showDialog.value = true
                        menuState.value = false
                    } ) {
                    Text(text = stringResource(id = R.string.filter))
                }
            }
        }

    )
}

@Composable
fun DefaultAppBar(onNavigationIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer"
                )
            }
        }
    )
}
