package com.example.seabattle.presentation.screens.game.resources

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.sp
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
        modifier = modifier
            .padding(bottom = dimensionResource(R.dimen.padding_big)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        CardPlayer(player = player1)
        CardPlayer(player = player2)
    }
}


@Composable
fun CardPlayer(
    player: Player,
){
    Card {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .sizeIn(
                    minWidth = 150.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
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
                    fontSize = 16.sp,
                    fontWeight = SemiBold,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_small))
                )

            }
            Box(
                modifier = Modifier
                    .size(16.dp)
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
