package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.repository.PreGameRepository
import kotlinx.coroutines.flow.Flow

class GetRoomsUseCase(
    val preGameRepository: PreGameRepository,
) {
    operator fun invoke(): Flow<Result<List<Room>>>  {
        return preGameRepository.fetchRooms()
    }
}