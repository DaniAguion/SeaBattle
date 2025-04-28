package com.example.seabattle.domain.room.usescases

import com.example.seabattle.domain.room.RoomRepository

class CreateRoomUseCase(
    val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return roomRepository.createRoom()
    }
}