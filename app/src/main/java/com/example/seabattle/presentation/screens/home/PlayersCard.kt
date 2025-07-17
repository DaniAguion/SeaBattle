package com.example.seabattle.presentation.screens.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleGame
import com.example.seabattle.domain.entity.Player
import com.example.seabattle.presentation.theme.SeaBattleTheme


@Composable
fun PlayerCard(
    modifier: Modifier,
    player: Player,
    inviteClick: (String) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = if (player.status == "online") colorResource(id = R.color.user_online_color) else Color.Gray,
                            shape = CircleShape
                        )
                )
                Text(
                    text = player.displayName ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                )
                Text(
                    text = "(${player.score})",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Button (
                shape = MaterialTheme.shapes.large,
                onClick = { inviteClick(player.userId) }
            ) {
                Text(
                    text = stringResource(id = R.string.invite_button),
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PlayerCardPreview(){
    SeaBattleTheme {
        PlayerCard(
            modifier = Modifier.fillMaxWidth(),
            player = sampleGame.player1,
        )
    }
}
