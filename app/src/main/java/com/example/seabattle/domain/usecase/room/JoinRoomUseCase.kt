package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Room
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
            val userId = session.getCurrentUserId()
            val user = userRepository.getUser(userId).getOrThrow()

            // Function to validate the room state and join the game.
            fun joinRoom(room: Room): Map<String, Any> {
                if (room.player1.userId == user.userId || room.numberOfPlayers == 2 ||
                    room.roomState != RoomState.WAITING_FOR_PLAYER.name) {
                    throw Exception("Room not available")
                }

                // Update the room state and add the second player
                return mapOf(
                    "roomState" to RoomState.SECOND_PLAYER_JOINED.name,
                    "numberOfPlayers" to 2,
                    "player2" to user.toBasic()
                )
            }

            // Update the room state to GAME_STARTING and join the game.
            roomRepository.updateRoomFields(roomId, ::joinRoom).getOrThrow()

            // Fetch the updated room and set it in the session
            val room = roomRepository.getRoom(roomId).getOrThrow()
            session.setCurrentRoom(room)
        }
    }
}