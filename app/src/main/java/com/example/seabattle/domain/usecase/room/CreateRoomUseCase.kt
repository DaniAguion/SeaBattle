package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.entity.toBasic
import com.example.seabattle.domain.repository.RoomRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.UUID

class CreateRoomUseCase(
    val roomRepository: RoomRepository,
    val userRepository: UserRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    suspend operator fun invoke(roomName: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = session.getCurrentUserId()
            val user = userRepository.getUser(userId).getOrThrow()

            if (user == null) {
                throw Exception("User not found")
            }

            // Create the room in the repository
            val roomId = UUID.randomUUID().toString()
            roomRepository.createRoom(roomId, roomName, user).getOrThrow()

            // Fetch the updated room and set it in the session
            val room = roomRepository.getRoom(roomId).getOrThrow()
            session.setCurrentRoom(room)
            return@runCatching
        }
    }
}