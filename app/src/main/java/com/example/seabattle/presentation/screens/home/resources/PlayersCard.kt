package com.example.seabattle.presentation.screens.home.resources


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleGame
import com.example.seabattle.domain.entity.Player
import com.example.seabattle.presentation.theme.SeaBattleTheme


@Composable
fun PlayerCard(
    modifier: Modifier,
    player: Player,
    enableInvite: Boolean,
    inviteClick: (String) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(dimensionResource(id = R.dimen.card_elevation)),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(all = dimensionResource(id = R.dimen.padding_small))
                .fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.7f)
            ){
                Box(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.status_size))
                        .background(
                            color = if (player.status == "online") colorResource(id = R.color.user_online_color) else Color.Gray,
                            shape = CircleShape
                        )
                )
                Text(
                    text = "${player.displayName} (${player.score})",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.padding_small),
                        end = dimensionResource(id = R.dimen.padding_xsmall)
                    )
                )
            }
            Button (
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_xsmall)),
                onClick = { inviteClick(player.userId) },
                enabled = enableInvite,
                modifier = Modifier.weight(0.3f)
            ) {
                Text(
                    text = stringResource(id = R.string.invite_button),
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



@Preview(showBackground = true)
@Composable
fun PlayerCardPreview(){
    SeaBattleTheme {
        PlayerCard(
            modifier = Modifier.fillMaxWidth(),
            player = sampleGame.player1,
            enableInvite = true,
        )
    }
}
