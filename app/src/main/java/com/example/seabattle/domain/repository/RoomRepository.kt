package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Room

interface RoomRepository {
    suspend fun fetchRooms(): Result<List<Room>>
    suspend fun getRoom(roomId: String): Result<Room?>
    suspend fun createRoom(room: Room): Result<Unit>
    suspend fun updateRoom(roomId: String, newData: Map<String, Any>) : Result<Unit>
    suspend fun deleteRoom(roomId: String): Result<Unit>
}