package com.example.seabattle.presentation.screens.leaderboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleBasicPlayersList
import com.example.seabattle.domain.entity.BasicPlayer
import com.example.seabattle.presentation.theme.SeaBattleTheme


@Composable
fun UserCard(
    user: BasicPlayer?,
    position: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        border = BorderStroke(
            width = if (position > 3 || position == 0) { Dp.Hairline } else { 4.dp },
            color = if (position == 1) {
                colorResource(id = R.color.gold_color)
            } else if (position == 2) {
                colorResource(id = R.color.silver_color)
            } else if (position == 3) {
                colorResource(id = R.color.bronze_color)
            } else {
                colorResource(id = R.color.none_color)
            }
        ),
        elevation = CardDefaults.cardElevation(dimensionResource(id = R.dimen.card_elevation))
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = position.toString() + "º",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small))
            )
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = user?.displayName ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.padding_xsmall))
                        .weight(0.7f),
                )
                Text(
                    text = user?.score.toString(),
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.padding_xsmall))
                        .weight(0.3f)

                )
            }
        }
    }
}

@Preview
@Composable
fun GameCardPreview() {
    SeaBattleTheme {
        UserCard(
            user = sampleBasicPlayersList.first(),
            position = 4,
            modifier = Modifier.fillMaxWidth()
        )
    }
}