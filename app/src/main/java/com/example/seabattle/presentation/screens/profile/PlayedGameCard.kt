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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.seabattle.R
import com.example.seabattle.data.local.gameSample1
import com.example.seabattle.domain.entity.GameHistory
import com.example.seabattle.domain.entity.User
import com.example.seabattle.presentation.theme.SeaBattleTheme


@Composable
fun PlayedGameCard(
    user: User,
    game: GameHistory,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ){
                Text(
                    text = if (user.userId == game.winnerId) {stringResource(id = R.string.victory)} else {stringResource(id = R.string.defeat)},
                    style = MaterialTheme.typography.titleMedium,
                    color = if (user.userId == game.winnerId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
                Text(
                    text = "( " + if (user.userId == game.player1.userId) {
                        game.player1.score.toString()
                    } else {
                        game.player2.score.toString()
                    } + if (game.scoreTransacted > 0) {" + "} else {" "} + "${game.scoreTransacted}" + " )",

                    style = MaterialTheme.typography.bodyMedium
                    .copy(fontWeight = SemiBold),
                    color = if (user.userId == game.winnerId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_small))
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                PlayerInfo(
                    player = game.player1
                )
                Text(
                    text = stringResource(id = R.string.vs),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = SemiBold
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                PlayerInfo(
                    player = game.player2
                )
            }
        }

    }
}


@Composable
fun PlayerInfo(
    player: User
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (player.photoUrl.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.account_box_40px),
                contentDescription = "User photo",
                modifier = Modifier.size(40.dp)
            )
        } else {
            AsyncImage(
                model = player.photoUrl,
                contentDescription = "User photo",
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.account_box_40px),
            )
        }

        Text(
            text = player.displayName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
        )
    }
}

@Preview
@Composable
fun GameCardPreview() {
    SeaBattleTheme {
        PlayedGameCard(
            user = gameSample1.player1,
            game = GameHistory(
                gameId = "1",
                winnerId = gameSample1.player1.userId,
                player1 = gameSample1.player1,
                player2 = gameSample1.player2,
                14,
                playedAt = gameSample1.updatedAt
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}