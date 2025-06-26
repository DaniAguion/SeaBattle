package com.example.seabattle.presentation.screens.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seabattle.R
import com.example.seabattle.presentation.theme.SeaBattleTheme

@Composable
fun LeaderBoardScreen(
    modifier: Modifier = Modifier
) {
    LeaderBoardContent(
        modifier = modifier
    )
}

@Composable
fun LeaderBoardContent(
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
    }
}


@Preview(showBackground = true)
@Composable
fun LeaderBoardPreview(){
    SeaBattleTheme {
        LeaderBoardContent(
            modifier = Modifier.fillMaxSize(),
        )
    }
}