package com.example.seabattle.presentation.screens.room

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.RoomState
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
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalActivity.current

    // Added to make sure the room is deleted if user closes the app
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                if (activity?.isChangingConfigurations == false) {
                    roomViewModel.onUserLeave()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Observe the room state and navigate to the game screen if the game was created
    LaunchedEffect(key1 = roomUiState.room) {
        val room = roomUiState.room
        if (room!= null && room.roomState == RoomState.GAME_CREATED.name) {
            navController.navigate(SeaBattleScreen.Game.title)
        }
    }

    RoomScreenContent(
        modifier = modifier,
        navController = navController,
        room = roomUiState.room,
        onUserLeave = roomViewModel::onUserLeave
    )
}

@Composable
fun RoomScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    room: Room?,
    onUserLeave: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // Header
        item{
            Text(
                text = "Waiting Room",
                fontSize = 20.sp,
                fontWeight = SemiBold,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }

        item{
            if (room != null) {
                Text(
                    text = "Room Name: ${room.roomName}",
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

        item{
            if (room != null && room.player2 == null) {
                Text(
                    text = "Waiting for player 2...",
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_big))
                )
                Button(
                    onClick = {
                        onUserLeave()
                        navController.navigate(SeaBattleScreen.Home.title)
                    },
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    Text(text = "Exit Room")
                }
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