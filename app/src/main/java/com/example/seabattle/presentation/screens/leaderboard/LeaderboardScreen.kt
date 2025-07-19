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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleBasicPlayersList
import com.example.seabattle.domain.entity.BasicPlayer
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
    user: BasicPlayer?,
    userPosition: Int?,
    usersList: List<BasicPlayer>,
    loadingList: Boolean,
    errorList: Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = dimensionResource(R.dimen.padding_container))
    ) {
        // Header Title and Description
        item {
            Text(
                text = stringResource(R.string.leaderboard_header_title),
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.padding_xsmall))
            )
            Text(
                text = stringResource(R.string.leaderboard_header_desc),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.padding_medium))
                    .fillMaxWidth()
            )
        }


        // Header for the user's position
        item{
            Text(
                text = stringResource(R.string.your_position),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = dimensionResource(R.dimen.padding_small))
            )
        }


        // User's card
        item{
            UserCard(
                user = user,
                position = userPosition ?: 0,
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.padding_medium))
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            )
        }


        // Header for the ranking list
        item{
            Text(
                text = stringResource(R.string.ranking_header),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = dimensionResource(R.dimen.padding_small))
            )
        }

        // Users list
        item {
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
                                .padding(bottom = dimensionResource(R.dimen.padding_xsmall))
                                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                        )
                    }
                }

                else -> {
                    Text(
                        text = stringResource(R.string.error_get_users),
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_xsmall)),
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
            user = sampleBasicPlayersList.first(),
            userPosition = 1,
            usersList = sampleBasicPlayersList,
            loadingList = false,
            errorList = false,
            modifier = Modifier.fillMaxSize(),
        )
    }
}