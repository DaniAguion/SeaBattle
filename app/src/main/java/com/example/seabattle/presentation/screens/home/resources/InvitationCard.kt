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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleInvitation
import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.presentation.theme.SeaBattleTheme


@Composable
fun InvitationCard(
    invitation: Invitation,
    onClickJoin: (String) -> Unit,
    onClickReject: (Invitation) -> Unit,
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
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically

        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(0.7f)
            ) {
                Text(
                    text = "By ${invitation.invitedBy.displayName} (${invitation.invitedBy.score})",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ){
                Button (
                    shape = MaterialTheme.shapes.large,
                    onClick = { onClickJoin(invitation.gameId) },
                    modifier = Modifier.padding(end = 4.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.join_button),
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Button (
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = { onClickReject(invitation) }
                ) {
                    Text(
                        text = stringResource(id = R.string.reject_button),
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium
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