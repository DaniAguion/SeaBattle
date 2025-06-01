package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.Ship
import com.example.seabattle.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface PreGameRepository {
    fun fetchRooms() : Flow<Result<List<Room>>>
    fun listenRoomUpdates(roomId: String) : Flow<Result<Room>>
    suspend fun createRoom(roomId: String, roomName: String, user: User) : Result<Unit>
    suspend fun joinRoom(roomId: String, user: User) : Result<Unit>
    suspend fun getRoom(roomId: String) : Result<Room>
    suspend fun deleteRoom(roomId: String) : Result<Unit>
    suspend fun createGame(roomId: String, logicFunction: (Room) -> Game, updatedRoomData: Map<String, Any>) : Result<String>
    suspend fun updateGameFields(roomId: String, logicFunction: (Room) -> Map<String, Any>): Result<Unit>
}