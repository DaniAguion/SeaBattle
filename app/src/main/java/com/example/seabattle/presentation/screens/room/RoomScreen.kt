package com.example.seabattle.presentation.screens.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.UserBasic
import com.example.seabattle.presentation.SeaBattleScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun RoomScreen(
    modifier: Modifier,
    navController: NavHostController,
    roomViewModel: RoomViewModel = koinViewModel(),
) {
    val roomUiState by roomViewModel.uiState.collectAsState()

    RoomScreenContent(
        modifier = modifier,
        navController = navController,
        room = roomUiState.room
    )
}

@Composable
fun RoomScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    room: Room?
) {
    LaunchedEffect(key1 = room) {
        if (room!= null && room.roomState == "GAME_CREATED") {
            navController.navigate(SeaBattleScreen.Game.title)
        }
    }

    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Room Screen",
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
        if (room != null) {
            Text(
                text = "Room ID: ${room.roomName}",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
            Text(
                text = "State: ${room.roomState}",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
            Text(
                text = "Player 1: ${room.player1.displayName}",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
            if (room.player2 != null) {
                Text(
                    text = "Player 2: ${room.player2.displayName}",
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}


@Preview (showBackground = true)
@Composable
fun RoomPreview(){
    RoomScreenContent(
        modifier = Modifier.fillMaxSize(),
        navController = NavHostController(context = LocalContext.current),
        room = Room(
                roomId = "roomId",
                player1 = UserBasic("userId", "displayName"),
                player2 = null,
                roomName = "roomName",
                roomState = "WAITING_FOR_PLAYER"
        )
    )
}