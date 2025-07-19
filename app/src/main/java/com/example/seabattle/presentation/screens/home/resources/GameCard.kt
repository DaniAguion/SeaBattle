package com.example.seabattle.presentation.screens.home.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seabattle.R
import com.example.seabattle.presentation.theme.SeaBattleTheme


@Composable
fun GameCard(
    playerName: String,
    gameId: String,
    score: Int,
    gameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(dimensionResource(id = R.dimen.card_elevation)),
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(all = dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth(),
        ){
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(0.7f)
                    .padding(end = dimensionResource(id = R.dimen.padding_small))
            ){
                Text(
                    text = "Against $playerName ($score)",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.padding_min))
                )
            }
            Button (
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_small)),
                onClick = { gameClick(gameId) },
                modifier = Modifier.weight(0.3f)

            ) {
                Text(
                    text = stringResource(id = R.string.join_button),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
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
            score = 1500,
            playerName = "MeuPixel",
            gameClick = {}
        )
    }
}