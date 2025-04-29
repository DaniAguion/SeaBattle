package com.example.seabattle.domain.room

import com.example.seabattle.domain.model.Room
import kotlin.collections.mutableListOf

interface RoomRepository {
    suspend fun createRoom(): Result<Unit>
    suspend fun getAllRooms(): Result<List<Room>>
}