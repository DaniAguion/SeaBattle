package com.example.seabattle.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.UserBasic
import com.example.seabattle.presentation.SeaBattleScreen
import com.example.seabattle.presentation.resources.RoomCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel  = koinViewModel()
) {
    val homeUiState by homeViewModel.uiState.collectAsState()

    HomeScreenContent(
        modifier = modifier,
        navController = navController,
        roomList = homeUiState.roomList,
        errorList = homeUiState.errorList,
        loadingList = homeUiState.loadingList,
        onClickCreateRoom = homeViewModel::onClickCreateRoom,
        onClickRefresh = homeViewModel::onClickRefresh
    )
}


@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    roomList : List<Room>,
    errorList : Boolean,
    loadingList : Boolean,
    onClickCreateRoom: () -> Unit,
    onClickRefresh: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home Screen"
        )
        Button(
            onClick = { onClickCreateRoom() }
        ) {
            Text(text = "Create Room")
        }
        Button(
            onClick = {
                navController.navigate(SeaBattleScreen.Game.title) {
                }
            }
        ) {
            Text(text = "Start Game")
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(
                        stringResource(R.string.list_rooms_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    IconButton(onClick = onClickRefresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Icon",
                        )
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                if (loadingList) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                } else if (!errorList) {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(items = roomList, key = { it.roomId }) { room ->
                            RoomCard(room = room)
                        }
                    }
                } else {
                    Text(
                        text = stringResource(R.string.error_get_rooms),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreenContent(
        modifier = Modifier.fillMaxSize(),
        navController = NavHostController(context = LocalContext.current),
        onClickCreateRoom = {  },
        onClickRefresh = {  },
        errorList = false,
        loadingList = false,
        roomList = listOf(
            Room(
                roomId = "1",
                roomName = "Room 1",
                roomState = "WAITING_FOR_PLAYER",
                numberOfPlayers = 1,
                player1 = UserBasic(
                    userId = "1",
                    displayName = "Player 1",
                    photoUrl = "",
                )
            ),
            Room(
                roomId = "2",
                roomName = "Room 2",
                roomState = "WAITING_FOR_PLAYER",
                numberOfPlayers = 1,
                player1 = UserBasic(
                    userId = "1",
                    displayName = "Player 1",
                    photoUrl = "",
                )
            )
        )
    )
}