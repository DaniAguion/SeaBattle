package com.example.seabattle.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import com.example.seabattle.domain.model.Room
import com.example.seabattle.domain.model.UserBasic
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
        onCreateRoomClick = homeViewModel::onClickCreateRoom,
    )
}


@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    roomList : List<Room>,
    errorList : Boolean,
    onCreateRoomClick: () -> Unit
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
            onClick = { onCreateRoomClick() }
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
        if (errorList) {
            Text(
                text = stringResource(R.string.error_get_rooms),
                modifier = Modifier.padding(8.dp)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items = roomList, key = { it.roomId }) { room ->
                    RoomCard(room = room)
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
        onCreateRoomClick = {  },
        errorList = false,
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