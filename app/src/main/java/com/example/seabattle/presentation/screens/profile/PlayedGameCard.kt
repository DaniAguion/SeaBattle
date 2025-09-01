package com.example.seabattle.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleGame
import com.example.seabattle.data.local.sampleGameHistory
import com.example.seabattle.domain.entity.BasicPlayer
import com.example.seabattle.domain.entity.GameHistory
import com.example.seabattle.presentation.theme.SeaBattleTheme


@Composable
fun PlayedGameCard(
    userId: String,
    game: GameHistory,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(dimensionResource(id = R.dimen.card_elevation)),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(all = dimensionResource(R.dimen.padding_small))
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = if (userId == game.winnerId) {stringResource(id = R.string.victory)} else {stringResource(id = R.string.defeat)},
                    style = MaterialTheme.typography.titleMedium,
                    color = if (userId == game.winnerId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_xsmall))
                )
                Text(
                    text = "( " + if (userId == game.player1.userId) {
                        game.player1.score.toString()
                    } else {
                        game.player2.score.toString()
                    } + if (game.scoreTransacted > 0) {" + "} else {" "} + "${game.scoreTransacted}" + " )",

                    style = MaterialTheme.typography.bodyMedium
                    .copy(fontWeight = SemiBold),
                    color = if (userId == game.winnerId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_xsmall))
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                PlayerInfo(
                    player = game.player1,
                    modifier = Modifier.weight(0.4f)
                )
                Text(
                    text = stringResource(id = R.string.vs),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = SemiBold
                    ),
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_small))
                )
                PlayerInfo(
                    player = game.player2,
                    modifier = Modifier.weight(0.4f)
                )
            }
        }

    }
}


@Composable
fun PlayerInfo(
    player: BasicPlayer,
    modifier: Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (player.photoUrl.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.account_box_40px),
                contentDescription = null,

            )
        } else {
            AsyncImage(
                model = player.photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.account_box_40px),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.profile_image_size))
            )
        }

        Text(
            text = player.displayName,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_xsmall))
        )
    }
}

@Preview
@Composable
fun GameCardPreview() {
    SeaBattleTheme {
        PlayedGameCard(
            userId = sampleGame.player1.userId,
            game = sampleGameHistory,
            modifier = Modifier.fillMaxWidth()
        )
    }
}