package com.example.seabattle.presentation.screens.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.UserBasic
import org.koin.androidx.compose.koinViewModel


@Composable
fun RoomScreen(
    modifier: Modifier,
    navController: NavHostController,
    roomViewModel: RoomViewModel = koinViewModel(),
) {
    val roomUiState by roomViewModel.uiState.collectAsState()

    ProfileScreenContent(
        modifier = modifier,
        roomUiState = roomUiState
    )
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    roomUiState: RoomUiState = RoomUiState()
) {
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
        if (roomUiState.room != null) {
            Text(
                text = "Room ID: ${roomUiState.room.roomName}",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
            Text(
                text = "State: ${roomUiState.room.roomState}",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
            Text(
                text = "Player 1: ${roomUiState.room.player1.displayName}",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
            if (roomUiState.room.player2 != null) {
                Text(
                    text = "Player 2: ${roomUiState.room.player2.displayName}",
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}


@Preview (showBackground = true)
@Composable
fun ProfilePreview(){
    ProfileScreenContent(
        modifier = Modifier.fillMaxSize(),
        roomUiState = RoomUiState(
            room = Room(
                roomId = "roomId",
                player1 = UserBasic("userId", "displayName"),
                player2 = null,
                roomName = "roomName",
                roomState = "roomState"
            )
        )
    )
}