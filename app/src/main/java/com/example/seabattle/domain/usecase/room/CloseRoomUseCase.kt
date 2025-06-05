package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.RoomError
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class CloseRoomUseCase(
    val roomRepository: RoomRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    suspend operator fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            Timber.d("CloseRoomUseCase invoked.")
            val roomId = session.getCurrentRoom()?.roomId

            if (roomId != null && roomId.isNotEmpty()){
                // Delete the room if it wasn't deleted yet and clear the session
                val room = roomRepository.getRoom(roomId).getOrNull()
                if (room != null) {
                    roomRepository.deleteRoom(roomId).getOrThrow()
                }
            }
            session.clearCurrentRoom()
        }
        .onFailure { e ->
            Timber.e(e, "CloseRoomUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is RoomError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}