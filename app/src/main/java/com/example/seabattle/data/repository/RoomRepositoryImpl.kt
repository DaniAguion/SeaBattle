package com.example.seabattle.data.repository

import com.example.seabattle.domain.auth.SessionManager
import com.example.seabattle.domain.firestore.FirestoreRepository
import com.example.seabattle.domain.model.Room
import com.example.seabattle.domain.model.RoomState
import com.example.seabattle.domain.model.toBasic
import com.example.seabattle.domain.room.RoomRepository
import java.util.UUID

class RoomRepositoryImpl(
    private val fireStoreRepository: FirestoreRepository,
    private val sessionManager: SessionManager
) : RoomRepository {

    override suspend fun createRoom(): Result<Unit> {
        val player1 = sessionManager.getFireStoreUserProfile()
            ?: return Result.failure(IllegalStateException("User not found"))
        val room = Room(
            roomId = UUID.randomUUID().toString(),
            roomName = "Room Name Test",
            roomState = RoomState.WAITING_FOR_PLAYER.name,
            numberOfPlayers = 1,
            player1 = player1.toBasic(),
        )
        return fireStoreRepository.createRoom(room)
    }


    override suspend fun getAllRooms(): Result<List<Room>> {
        return fireStoreRepository.fetchRooms()
    }

}