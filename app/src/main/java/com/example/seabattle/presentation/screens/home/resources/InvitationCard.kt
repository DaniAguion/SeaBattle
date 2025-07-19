package com.example.seabattle.presentation.screens.home.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleInvitation
import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.presentation.theme.SeaBattleTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.text.style.TextAlign

@Composable
fun InvitationCard(
    invitation: Invitation,
    onClickJoin: (String) -> Unit,
    onClickReject: (Invitation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(dimensionResource(id = R.dimen.card_elevation)),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = R.dimen.padding_xsmall),
                    horizontal = dimensionResource(id = R.dimen.padding_small)
                )

        ){
            Text(
                text = "By ${invitation.invitedBy.displayName} (${invitation.invitedBy.score})",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = dimensionResource(id = R.dimen.padding_xsmall))
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(0.5f)
            ){
                Button (
                    shape = MaterialTheme.shapes.medium,
                    onClick = { onClickJoin(invitation.gameId) },
                    contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_xsmall)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = dimensionResource(id = R.dimen.padding_min))
                ) {
                    Text(
                        text = stringResource(id = R.string.join_button),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Button (
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_xsmall)),
                    onClick = { onClickReject(invitation) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.reject_button),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun InvitationCardPreview() {
    SeaBattleTheme {
        InvitationCard(
            invitation = sampleInvitation,
            onClickJoin = {},
            onClickReject = {}
        )
    }
}