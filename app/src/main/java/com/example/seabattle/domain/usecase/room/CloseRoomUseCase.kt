package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.repository.PreGameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CloseRoomUseCase(
    val preGameRepository: PreGameRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    suspend operator fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val roomId = session.getCurrentRoom()?.roomId

            if (roomId == null || roomId.isEmpty()){
                throw Exception("Room is not set")
            }

            // Delete the room if it wasn't deleted yet and clear the session
            val room = preGameRepository.getRoom(roomId).getOrNull()
            if (room != null) {
                preGameRepository.deleteRoom(roomId).getOrThrow()
            }
            session.clearCurrentRoom()
        }
    }
}