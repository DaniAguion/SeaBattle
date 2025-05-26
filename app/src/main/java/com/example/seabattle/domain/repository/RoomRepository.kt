package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun fetchRooms() : Flow<Result<List<Room>>>
    fun listenRoomUpdates(roomId: String) : Flow<Result<Room>>
    suspend fun createRoom(roomId: String, roomName: String, user: User) : Result<Unit>
    suspend fun joinRoom(roomId: String, user: User) : Result<Unit>
    suspend fun getRoom(roomId: String) : Result<Room>
    suspend fun deleteRoom(roomId: String) : Result<Unit>
    suspend fun createGame(gameId: String, roomId: String) : Result<Unit>
    suspend fun joinGame(gameId: String, roomId: String) : Result<Game>
}