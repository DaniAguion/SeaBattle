package com.example.seabattle.presentation.screens.leaderboard

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seabattle.R
import com.example.seabattle.domain.entity.User
import com.example.seabattle.presentation.resources.toErrorMessageUI
import com.example.seabattle.presentation.theme.SeaBattleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LeaderBoardScreen(
    modifier: Modifier = Modifier,
    leaderboardViewModel: LeaderboardViewModel  = koinViewModel()
) {
    val leaderboardUiState by leaderboardViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Load the leaderboard data when the screen is first composed
    DisposableEffect(Unit) {
        leaderboardViewModel.getUserPosition()
        leaderboardViewModel.getLeaderboard()
        onDispose {
            leaderboardViewModel.onErrorShown()
        }
    }


    // Show a toast message when an error occurs
    LaunchedEffect(key1 = leaderboardUiState.error) {
        leaderboardUiState.error?.let { error ->
            Toast.makeText(context, context.getString(error.toErrorMessageUI()), Toast.LENGTH_LONG).show()
            leaderboardViewModel.onErrorShown()
        }
    }


    LeaderBoardContent(
        user = leaderboardUiState.user,
        userPosition = leaderboardUiState.userPosition,
        usersList = leaderboardUiState.usersList,
        loadingList = leaderboardUiState.loadingList,
        errorList = leaderboardUiState.errorList,
        modifier = modifier
    )
}

@Composable
fun LeaderBoardContent(
    user: User?,
    userPosition: Int?,
    usersList: List<User>,
    loadingList: Boolean,
    errorList: Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        item {
            Text(
                text = stringResource(R.string.leaderboard_header_title),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 6.dp)
            )
        }
        item {
            Text(
                text = stringResource(R.string.leaderboard_header_desc),
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }
        item {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.your_position),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    )
                    UserCard(
                        user = user,
                        position = userPosition ?: 0,
                        modifier = Modifier
                    )
                }
            }
        }
        item {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.ranking_header),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    )
                    when {
                        loadingList -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }

                        !errorList -> {
                            usersList.forEach { user ->
                                UserCard(
                                    user = user,
                                    position = usersList.indexOf(user) + 1,
                                    modifier = Modifier
                                )
                            }
                        }

                        else -> {
                            Text(
                                text = stringResource(R.string.error_get_users),
                                modifier = Modifier.padding(8.dp),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LeaderBoardPreview(){
    SeaBattleTheme {
        LeaderBoardContent(
            user = User(userId = "1", displayName = "Player1", score = 100),
            userPosition = 1,
            usersList = listOf(
                User(userId = "1", displayName = "Player1", score = 100),
                User(userId = "2", displayName = "Player2", score = 90),
                User(userId = "3", displayName = "Player3", score = 80),
                User(userId = "4", displayName = "Player4", score = 70),
                User(userId = "5", displayName = "Player5", score = 60)
            ),
            loadingList = false,
            errorList = false,
            modifier = Modifier.fillMaxSize(),
        )
    }
}