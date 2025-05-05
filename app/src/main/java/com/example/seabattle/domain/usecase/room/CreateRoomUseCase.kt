package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.entity.toBasic
import com.example.seabattle.domain.repository.RoomRepository
import com.example.seabattle.domain.services.SessionManager
import java.util.UUID

class CreateRoomUseCase(
    val roomRepository: RoomRepository,
    val sessionManager: SessionManager
) {
    suspend operator fun invoke(): Result<Unit> {
        val player1 = sessionManager.getFireStoreUserProfile()
            ?: return Result.failure(IllegalStateException("User not found"))
        val room = Room(
            roomId = UUID.randomUUID().toString(),
            roomName = "Room Name Test",
            roomState = RoomState.WAITING_FOR_PLAYER.name,
            numberOfPlayers = 1,
            player1 = player1.toBasic(),
        )
        return roomRepository.createRoom(room)
    }
}