package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class GetRoomsUseCase(
    val roomRepository: RoomRepository,
) {
    operator fun invoke(): Flow<Result<List<Room>>>  {
        return roomRepository.fetchRooms()
    }
}