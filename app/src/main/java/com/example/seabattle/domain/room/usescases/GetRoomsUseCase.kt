package com.example.seabattle.domain.room.usescases

import com.example.seabattle.domain.model.Room
import com.example.seabattle.domain.room.RoomRepository

class GetRoomsUseCase(
    val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(): Result<List<Room>> {
        return roomRepository.getAvailableRooms()
    }
}