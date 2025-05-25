package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun fetchRooms() : Flow<Result<List<Room>>>
    fun listenRoomUpdates(roomId: String) : Flow<Result<Room>>
    suspend fun createRoom(room: Room) : Result<Unit>
    suspend fun joinRoom(roomId: String, user: User) : Result<Unit>
    suspend fun getRoom(roomId: String) : Result<Room>
    suspend fun updateRoom(roomId: String, newData: Map<String, Any>) : Result<Unit>
    suspend fun deleteRoom(roomId: String) : Result<Unit>
    suspend fun updateRoomState(roomId: String, userId: String) : Result<Unit>
}