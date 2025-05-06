package com.example.seabattle.presentation.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.User
import com.example.seabattle.domain.entity.toBasic

@Composable
fun RoomCard(
    room: Room,
    roomClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ){
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = room.roomName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Play against: ${room.player1.displayName}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Button (
                onClick = { roomClick(room.roomId) },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(100.dp)
            ) {
                Text(text = "Join", fontSize = 16.sp)
            }
        }
    }
}

@Preview
@Composable
fun RoomCardPreview() {
    RoomCard(
        room = Room(
            roomId = "1",
            roomName = "Room 1",
            roomState = "WAITING_FOR_PLAYER",
            numberOfPlayers = 2,
            player1 = User(
                userId = "1",
                displayName = "Peter",
            ).toBasic()
        ),
        roomClick = {}
    )
}