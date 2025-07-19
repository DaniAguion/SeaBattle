package com.example.seabattle.presentation.screens.game.resources

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleGame
import com.example.seabattle.domain.entity.Player
import com.example.seabattle.presentation.theme.SeaBattleTheme


@Composable
fun PlayersInfoHeader(
    modifier: Modifier,
    player1: Player,
    player2: Player
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        PlayerStatusCard(
            player = player1,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
                .weight(1f)
        )
        PlayerStatusCard(
            player = player2,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
                .weight(1f)
        )
    }
}


@Composable
fun PlayerStatusCard(
    player: Player,
    modifier: Modifier = Modifier
){
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(dimensionResource(id = R.dimen.card_elevation)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f)
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
                    maxLines = 3,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = SemiBold,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_small))
                )

            }
            Box(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.status_size))
                    .background(
                        color = if (player.status == "online") colorResource(id = R.color.user_online_color) else Color.Gray,
                        shape = CircleShape
                    )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PlayersInfoHeaderPreview(){
    SeaBattleTheme {
        PlayersInfoHeader(
            modifier = Modifier.fillMaxSize(),
            player1 = sampleGame.player1,
            player2 = sampleGame.player2
        )
    }
}
