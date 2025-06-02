package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CloseRoomUseCase(
    val roomRepository: RoomRepository,
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
            val room = roomRepository.getRoom(roomId).getOrNull()
            if (room != null) {
                roomRepository.deleteRoom(roomId).getOrThrow()
            }
            session.clearCurrentRoom()
        }
    }
}