package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.entity.toBasic
import com.example.seabattle.domain.repository.RoomRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.UUID

class CreateRoomUseCase(
    val roomRepository: RoomRepository,
    val userRepository: UserRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    suspend operator fun invoke(roomName: String): Result<Room> = withContext(ioDispatcher) {
        runCatching {
            val playerId = session.getCurrentUserId()
            val player1 = userRepository.getUser(playerId).getOrThrow()
            if (player1 == null) {
                throw Exception("User not found")
            }
            val room = Room(
                roomId = UUID.randomUUID().toString(),
                roomName = roomName,
                roomState = RoomState.WAITING_FOR_PLAYER.name,
                numberOfPlayers = 1,
                player1 = player1.toBasic(),
            )
            roomRepository.createRoom(room).getOrThrow()
            val createdRoom = roomRepository.getRoom(room.roomId).getOrThrow()
            session.setCurrentRoom(createdRoom)
            return@runCatching createdRoom
        }
    }
}