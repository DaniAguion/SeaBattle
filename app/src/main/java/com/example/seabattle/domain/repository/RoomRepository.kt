package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun fetchRooms() : Flow<Result<List<Room>>>
    fun listenRoomUpdates(roomId: String) : Flow<Result<Room?>>
    suspend fun createRoom(room: Room) : Result<Unit>
    suspend fun getRoom(roomId: String) : Result<Room>
    suspend fun updateRoomFields(roomId: String, logicFunction: (Room) -> Map<String, Any>): Result<Unit>
    suspend fun deleteRoom(roomId: String) : Result<Unit>
}