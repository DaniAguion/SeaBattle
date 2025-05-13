package com.example.seabattle.presentation.screens.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.UserBasic
import com.example.seabattle.presentation.SeaBattleScreen
import com.example.seabattle.presentation.resources.RoomCard
import com.example.seabattle.presentation.validation.ValidationError
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel  = koinViewModel()
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current

    HomeScreenContent(
        modifier = modifier,
        navController = navController,
        context = context,
        roomName = homeUiState.roomName,
        roomNameError = homeUiState.roomNameError,
        roomList = homeUiState.roomList,
        errorList = homeUiState.errorList,
        loadingList = homeUiState.loadingList,
        hasJoined = homeUiState.hasJoined,
        actionFailed = homeUiState.actionFailed,
        onRoomNameUpdate = homeViewModel::onRoomNameUpdate,
        onClickCreateRoom = homeViewModel::onClickCreateRoom,
        onClickJoinRoom = homeViewModel::onClickJoinRoom,
    )
}


@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context,
    roomName : String,
    roomNameError : ValidationError?,
    roomList : List<Room>,
    errorList : Boolean,
    loadingList : Boolean,
    hasJoined: Boolean,
    actionFailed: Boolean,
    onRoomNameUpdate: (String) -> Unit,
    onClickCreateRoom: (String) -> Unit,
    onClickJoinRoom: (String) -> Unit,
) {
    LaunchedEffect(key1 = hasJoined) {
        if (hasJoined) {
            navController.navigate(SeaBattleScreen.Room.title)
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome!",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 4.dp)
                .padding(horizontal = 24.dp)

        )
        Text(
            text = "Join a room or create a new one to start playing",
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .padding(horizontal = 24.dp)
        )
        Column(
            modifier = Modifier
                .height(280.dp)
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.create_room_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )

            Card(
                modifier = modifier.padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = roomName,
                        onValueChange = onRoomNameUpdate,
                        label = { Text(stringResource(R.string.roomName)) },
                        singleLine = true,
                        isError = roomNameError != null,
                        supportingText = {
                            roomNameError?.let {
                                Text(
                                    text = stringResource(R.string.error_room_name),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)),
                    )
                    Button(
                        onClick = {
                            onClickCreateRoom(roomName)
                        }
                    ) {
                        Text(
                            text = "Create room",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
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
                        modifier = Modifier.padding(8.dp)
                    )
                }
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
                            RoomCard(
                                room = room,
                                roomClick = onClickJoinRoom
                            )
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

        LaunchedEffect(key1 = actionFailed) {
            Toast.makeText(
                context,
                context.getString(R.string.error_action),
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreenContent(
        modifier = Modifier.fillMaxSize(),
        navController = NavHostController(context = LocalContext.current),
        context = LocalContext.current,
        roomName = "Test Room",
        roomNameError = null,
        errorList = false,
        loadingList = false,
        hasJoined = false,
        actionFailed = false,
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
        ),
        onClickCreateRoom = { },
        onClickJoinRoom = { },
        onRoomNameUpdate = { },
    )
}