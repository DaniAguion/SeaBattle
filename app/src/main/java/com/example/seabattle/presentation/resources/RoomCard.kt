package com.example.seabattle.presentation.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.seabattle.domain.entity.Room

@Composable
fun RoomCard(
    room: Room,
    roomClick: (String) -> Unit,
    modifier: Modifier = Modifier
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
            verticalAlignment = Alignment.Bottom

        ){
            Column {
                Text(
                    text = room.roomName,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Estado: ${room.roomState}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Jugadores: ${room.numberOfPlayers}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Button(
                onClick = { roomClick(room.roomId) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = "Unirse")
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
            roomName = "Sala 1",
            roomState = "Abierta",
            numberOfPlayers = 2
        ),
        roomClick = {}
    )
}