package com.example.seabattle.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleGame
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Player
import com.example.seabattle.presentation.screens.Screen
import com.example.seabattle.presentation.theme.SeaBattleTheme
import com.example.seabattle.presentation.resources.toErrorMessageUI
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel  = koinViewModel()
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Stop listeners when the screen is disposed
    DisposableEffect(Unit) {
        homeViewModel.startListeners()
        onDispose {
            homeViewModel.stopListeners()
            homeViewModel.resetUiState()
        }
    }

    // Show a toast message when an error occurs
    LaunchedEffect(key1 = homeUiState.error) {
        homeUiState.error?.let { error ->
            Toast.makeText(context, context.getString(error.toErrorMessageUI()), Toast.LENGTH_LONG).show()
            homeViewModel.onErrorShown()
        }
    }


    // Navigate to Game screen when the user joins a game
    LaunchedEffect(key1 = homeUiState.hasJoined) {
        if (homeUiState.hasJoined) {
            navController.navigate(Screen.Game.title)
        }
    }

    HomeScreenContent(
        modifier = modifier,
        searchedUser = homeUiState.searchedUser,
        playersList = homeUiState.playersList,
        loadingPlayersList = homeUiState.loadingPlayersList,
        errorPlayersList = homeUiState.errorPlayersList,
        gamesList = homeUiState.gamesList,
        errorGamesList = homeUiState.errorGameList,
        loadingGamesList = homeUiState.loadingGamesList,
        onClickCreateGame = homeViewModel::onClickCreateGame,
        onUserSearchChange = homeViewModel::onUserSearchChange,
        onClickInviteUser = { },
        onClickJoinGame = homeViewModel::onClickJoinGame,
    )
}


@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    searchedUser: String,
    playersList: List<Player>,
    loadingPlayersList: Boolean,
    errorPlayersList: Boolean,
    gamesList : List<Game>,
    loadingGamesList : Boolean,
    errorGamesList : Boolean,
    onClickCreateGame: () -> Unit,
    onUserSearchChange: (String) -> Unit,
    onClickInviteUser: (String) -> Unit,
    onClickJoinGame: (String) -> Unit,
) {

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize(),

    ) {
        // Header
        item {
            Text(
                text = stringResource(R.string.home_header_title),
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp, bottom = 12.dp)
                    .padding(horizontal = 24.dp)
            )
            Text(
                text = stringResource(R.string.home_header_desc),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 12.dp)
                    .fillMaxWidth()
            )
        }


        // Create Game
        item {
            Button(
                onClick = { onClickCreateGame() },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp),
            ) {
                Text(
                    text = stringResource(R.string.create_game),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }


        // Invite an User
        item{
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                elevation = CardDefaults.cardElevation(16.dp),
                modifier = Modifier
                    .padding(all = 24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.invite_to_play_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                )
                OutlinedTextField(
                    value = searchedUser,
                    onValueChange = onUserSearchChange,
                    label = { Text(stringResource(R.string.search_user)) },
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 24.dp)
                        .fillMaxWidth()
                )

            }
        }

        // List of Users
        when {
            loadingPlayersList -> {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
            !errorPlayersList -> {
                if (playersList.isEmpty() && searchedUser.isNotEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(R.string.user_not_found),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),

                                )
                        }
                    }
                }

                items(items = playersList, key = { it.userId }) { game ->
                    Text(
                        text = game.displayName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                item {
                    Text(
                        text = stringResource(R.string.error_get_users),
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }


        
        // List of Games
        item{
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                elevation = CardDefaults.cardElevation(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Text(
                    text = stringResource(R.string.list_games_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp)
                )
            }
        }

        when {
            loadingGamesList -> {
                item {
                    HorizontalDivider(thickness = 2.dp)
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
            !errorGamesList -> {
                if (gamesList.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(R.string.no_available_games),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),

                            )
                        }
                    }
                } else {
                    item {
                        HorizontalDivider(thickness = 2.dp)
                    }
                }

                items(items = gamesList, key = { it.gameId }) { game ->
                    GameCard(
                        gameId = game.gameId,
                        playerName = game.player1.displayName,
                        gameClick = onClickJoinGame,
                        modifier = Modifier
                    )
                }
            }
            else -> {
                item {
                    Text(
                        text = stringResource(R.string.error_get_games),
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    SeaBattleTheme{
        HomeScreenContent(
            modifier = Modifier.fillMaxSize(),
            searchedUser = "",
            playersList = emptyList(),
            loadingPlayersList = false,
            errorPlayersList = false,
            gamesList = listOf(sampleGame),
            loadingGamesList = false,
            errorGamesList = false,
            onClickCreateGame = { },
            onUserSearchChange = { },
            onClickInviteUser = { },
            onClickJoinGame = { }
        )
    }
}