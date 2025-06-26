package com.example.seabattle.presentation.screens.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.seabattle.presentation.screens.home.GameCard
import com.example.seabattle.presentation.theme.SeaBattleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LeaderBoardScreen(
    modifier: Modifier = Modifier,
    leaderboardViewModel: LeaderboardViewModel  = koinViewModel()
) {
    val leaderboardUiState by leaderboardViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LeaderBoardContent(
        usersList = leaderboardUiState.usersList,
        loadingList = leaderboardUiState.loadingList,
        errorList = leaderboardUiState.errorList,
        modifier = modifier
    )
}

@Composable
fun LeaderBoardContent(
    usersList: List<User>,
    loadingList: Boolean,
    errorList: Boolean,
    modifier: Modifier = Modifier
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
                text = stringResource(R.string.leaderboard_header_title),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 14.dp)
            )
        }
        item{
            Text(
                text = stringResource(R.string.leaderboard_header_desc),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
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
                items(items = usersList, key = { it.userId }) { user ->
                    UserCard(
                        user = user,
                        modifier = Modifier
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
    }
}


@Preview(showBackground = true)
@Composable
fun LeaderBoardPreview(){
    SeaBattleTheme {
        LeaderBoardContent(
            usersList = listOf(
                User(userId = "1", displayName = "Player1", score = 100),
                User(userId = "2", displayName = "Player2", score = 90),
                User(userId = "3", displayName = "Player3", score = 80)
            ),
            loadingList = false,
            errorList = false,
            modifier = Modifier.fillMaxSize(),
        )
    }
}