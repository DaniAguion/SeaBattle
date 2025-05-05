package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.repository.RoomRepository

class GetRoomsUseCase(
    val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(): Result<List<Room>> {
        return roomRepository.fetchRooms()
    }
}