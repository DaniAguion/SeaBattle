package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.repository.PreGameRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class JoinRoomUseCase(
    val preGameRepository: PreGameRepository,
    val userRepository: UserRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    suspend operator fun invoke(roomId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = session.getCurrentUserId()
            val user = userRepository.getUser(userId).getOrThrow()

            if (user == null) {
                throw Exception("User not found")
            }

            // Update room state and add user to the room
            preGameRepository.joinRoom(roomId, user).getOrThrow()

            // Fetch the updated room and set it in the session
            val room = preGameRepository.getRoom(roomId).getOrThrow()
            session.setCurrentRoom(room)
        }
    }
}