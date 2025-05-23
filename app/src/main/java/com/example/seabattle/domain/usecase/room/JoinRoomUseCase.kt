package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.entity.toBasic
import com.example.seabattle.domain.repository.RoomRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class JoinRoomUseCase(
    val roomRepository: RoomRepository,
    val userRepository: UserRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    suspend operator fun invoke(roomId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val playerId = session.getCurrentUserId()
            val player2 = userRepository.getUser(playerId).getOrThrow()
            if (player2 == null) {
                throw Exception("User not found")
            }

            val room = roomRepository.getRoom(roomId).getOrThrow()

            if (
                room.player1.userId == player2.userId ||
                room.numberOfPlayers == 2 ||
                room.roomState != RoomState.WAITING_FOR_PLAYER.name
            ) {
                throw Exception("Room not available")
            }

            val newData = mapOf(
                "roomState" to RoomState.SECOND_PLAYER_JOINED.name,
                "numberOfPlayers" to 2,
                "player2" to player2.toBasic(),
            )

            roomRepository.updateRoom(roomId, newData).getOrThrow()
            val newRoom = roomRepository.getRoom(roomId).getOrThrow()
            return@runCatching session.setCurrentRoom(newRoom)
        }
    }
}