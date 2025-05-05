package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Room

interface RoomRepository {
    suspend fun fetchRooms(): Result<List<Room>>
    suspend fun createRoom(room: Room): Result<Unit>
    suspend fun updateRoom(room: Room): Result<Room?>
    suspend fun deleteRoom(roomId: String): Result<Unit>
}