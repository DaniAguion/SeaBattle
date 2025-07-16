package com.example.seabattle.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleGame
import com.example.seabattle.domain.entity.Game
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
        gameList = homeUiState.gamesList,
        errorList = homeUiState.errorList,
        loadingList = homeUiState.loadingList,
        onClickCreateGame = homeViewModel::onClickCreateGame,
        onClickJoinGame = homeViewModel::onClickJoinGame,
    )
}


@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    gameList : List<Game>,
    errorList : Boolean,
    loadingList : Boolean,
    onClickCreateGame: () -> Unit,
    onClickJoinGame: (String) -> Unit,
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        item {
            Text(
                text = stringResource(R.string.home_header_title),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 14.dp)
            )
            Text(
                text = stringResource(R.string.home_header_desc),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
        }


        // Create Game
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.create_game_title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(
                    onClick = { onClickCreateGame() },
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_small))
                ) {
                    Text(
                        text = stringResource(R.string.create_game),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }


        // Games List
        item {
            Text(
                text = stringResource(R.string.list_games_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        when {
            loadingList -> {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
            !errorList -> {
                items(items = gameList, key = { it.gameId }) { game ->
                    GameCard(
                        game = game,
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
            errorList = false,
            loadingList = false,
            gameList = listOf(sampleGame),
            onClickCreateGame = { },
            onClickJoinGame = { }
        )
    }
}