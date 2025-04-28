package com.example.seabattle.domain.room

interface RoomRepository {
    suspend fun createRoom(): Result<Unit>
}