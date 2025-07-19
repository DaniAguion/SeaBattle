package com.example.seabattle.presentation.screens.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleGame
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.domain.entity.Player
import com.example.seabattle.presentation.screens.Screen
import com.example.seabattle.presentation.theme.SeaBattleTheme
import com.example.seabattle.presentation.resources.toErrorMessageUI
import com.example.seabattle.presentation.screens.home.resources.GameCard
import com.example.seabattle.presentation.screens.home.resources.InvitationCard
import com.example.seabattle.presentation.screens.home.resources.PlayerCard
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
        invitationsList = homeUiState.invitationsList,
        playersList = homeUiState.playersList,
        searchDone = homeUiState.searchDone,
        loadingPlayersList = homeUiState.loadingPlayersList,
        errorPlayersList = homeUiState.errorPlayersList,
        gamesList = homeUiState.gamesList,
        errorGamesList = homeUiState.errorGameList,
        loadingGamesList = homeUiState.loadingGamesList,
        onClickCreateGame = homeViewModel::onClickCreateGame,
        onUserSearchChange = homeViewModel::onUserSearchChange,
        onUserSearch = homeViewModel::onUserSearch,
        onClickInviteUser = homeViewModel::onClickInviteUser,
        onClickJoinGame = homeViewModel::onClickJoinGame,
        onClickReject = homeViewModel::onClickRejectInvitation,
    )
}


@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    invitationsList: List<Invitation>,
    searchedUser: String,
    playersList: List<Player>,
    searchDone: Boolean,
    loadingPlayersList: Boolean,
    errorPlayersList: Boolean,
    gamesList : List<Game>,
    loadingGamesList : Boolean,
    errorGamesList : Boolean,
    onClickCreateGame: () -> Unit,
    onUserSearchChange: (String) -> Unit,
    onUserSearch: () -> Unit,
    onClickInviteUser: (String) -> Unit,
    onClickJoinGame: (String) -> Unit,
    onClickReject: (Invitation) -> Unit,
) {
    var expandedOptions by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize()
    ){
        // Header and Create Game Button
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimensionResource(R.dimen.padding_container))
        ) {
            // Header Title and Description
            item {
                Text(
                    text = stringResource(R.string.home_header_title),
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(R.dimen.padding_xsmall))
                )
                Text(
                    text = stringResource(R.string.home_header_desc),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = dimensionResource(R.dimen.padding_medium))
                        .fillMaxWidth()
                )
            }

            // Create Game Button
            item {
                Button(
                    onClick = { onClickCreateGame() },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .padding(bottom = dimensionResource(R.dimen.padding_large))
                    ,
                ) {
                    Text(
                        text = stringResource(R.string.create_game),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_min))
                    )
                }
            }

            // Invite User Section
            item {
                Text(
                    text = stringResource(R.string.invite_to_play_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = dimensionResource(R.dimen.padding_xsmall))
                        .fillMaxSize()
                )
                OutlinedTextField(
                    value = searchedUser,
                    onValueChange = onUserSearchChange,
                    label = { Text(stringResource(R.string.search_user)) },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { onUserSearch() }
                    ),
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(R.dimen.padding_medium))
                        .fillMaxWidth()
                )
            }


            // List of Players
            when {
                loadingPlayersList -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier.size(dimensionResource(R.dimen.progress_size)),
                            strokeWidth = 2.dp
                        )
                    }
                }

                !errorPlayersList -> {
                    if (searchDone && playersList.isEmpty()) {
                        item {
                            Card(
                                shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                                elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
                                modifier = modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    text = stringResource(R.string.user_not_found),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(dimensionResource(R.dimen.padding_medium)),

                                    )
                            }
                        }
                    }

                    items(items = playersList, key = { it.userId }) { player ->
                        PlayerCard(
                            player = player,
                            inviteClick = onClickInviteUser,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = dimensionResource(R.dimen.padding_xsmall)),
                        )
                    }
                }

                else -> {
                    item {
                        Text(
                            text = stringResource(R.string.error_get_users),
                            modifier = Modifier.padding(dimensionResource(R.dimen.padding_xsmall)),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }



            // Invitation Section
            item{
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { expandedOptions = !expandedOptions }
                        .fillMaxWidth()
                        .padding(
                            top= dimensionResource(R.dimen.padding_small),
                            bottom = dimensionResource(R.dimen.padding_medium)
                        )
                ){
                    Text(
                        text = stringResource(R.string.invitations_title) + " (${invitationsList.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        imageVector = if (expandedOptions) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expandedOptions) "Collapse" else "Expand"
                    )
                }
            }


            // Invitations List
            item{
                AnimatedVisibility(
                    visible = expandedOptions,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    if (invitationsList.isEmpty()) {
                        Card(
                            shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(bottom = dimensionResource(R.dimen.padding_xsmall))
                        ) {
                            Text(
                                text = stringResource(R.string.no_invitations),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(dimensionResource(R.dimen.padding_medium)),
                            )
                        }
                    } else {
                         invitationsList.forEach { invitation ->
                             InvitationCard(
                                invitation = invitation,
                                onClickJoin = onClickJoinGame,
                                onClickReject = onClickReject,
                                modifier = Modifier
                                    .padding(bottom = dimensionResource(R.dimen.padding_xsmall))
                            )
                        }
                    }
                }
            }


            // Available Games Section
            item{
                Text(
                    text = stringResource(R.string.list_games_title) + " (${gamesList.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = dimensionResource(R.dimen.padding_medium),
                            bottom = dimensionResource(R.dimen.padding_small)
                        )
                )
            }


            // List of Games
            when {
                loadingGamesList -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier.size(dimensionResource(R.dimen.progress_size)),
                            strokeWidth = 2.dp
                        )
                    }
                }
                !errorGamesList -> {
                    if (gamesList.isEmpty()) {
                        item {
                            Card(
                                shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                                elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(bottom = dimensionResource(R.dimen.padding_xsmall))
                            ) {
                                Text(
                                    text = stringResource(R.string.no_games_available),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(dimensionResource(R.dimen.padding_medium)),

                                    )
                            }
                        }
                    }

                    items(items = gamesList, key = { it.gameId }) { game ->
                        GameCard(
                            gameId = game.gameId,
                            playerName = game.player1.displayName,
                            score = game.player1.score,
                            gameClick = onClickJoinGame,
                            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_xsmall))
                        )
                    }
                }
                else -> {
                    item {
                        Text(
                            text = stringResource(R.string.error_get_games),
                            modifier = Modifier.padding(dimensionResource(R.dimen.padding_xsmall)),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
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
            invitationsList = emptyList(),
            playersList = emptyList(),
            searchDone = false,
            loadingPlayersList = false,
            errorPlayersList = false,
            gamesList = listOf(
                sampleGame,
                sampleGame.copy(gameId = "2"),
                sampleGame.copy(gameId = "3")
            ),
            loadingGamesList = false,
            errorGamesList = false,
            onClickCreateGame = { },
            onUserSearchChange = { },
            onUserSearch = { },
            onClickInviteUser = { },
            onClickJoinGame = { },
            onClickReject = { _ -> }
        )
    }
}