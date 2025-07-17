package com.example.seabattle.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seabattle.R
import com.example.seabattle.presentation.theme.SeaBattleTheme


@Composable
fun GameCard(
    playerName: String,
    gameId: String,
    gameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically

        ){
            Text(
                text = "Game of $playerName",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(0.7f),
            )
            Button (
                shape = MaterialTheme.shapes.large,
                onClick = { gameClick(gameId) }
            ) {
                Text(
                    text = stringResource(id = R.string.join_button),
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun GameCardPreview() {
    SeaBattleTheme {
        GameCard(
            gameId = "2",
            playerName = "MeuPixel",
            gameClick = {}
        )
    }
}