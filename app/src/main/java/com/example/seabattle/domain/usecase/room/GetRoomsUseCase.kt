package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow


class GetRoomsUseCase(
    val roomRepository: RoomRepository,
    val session: Session,
) {
    operator fun invoke(): Flow<Result<List<Room>>> {
        val userId = session.getCurrentUserId()
        return roomRepository.fetchRooms(userId)
    }
}