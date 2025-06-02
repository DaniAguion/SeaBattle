package com.example.seabattle.domain.usecase.room


import com.example.seabattle.domain.Session
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.RoomError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class ListenRoomUseCase(
    val roomRepository: RoomRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    // This function listen the room entity and updates the local room object
    suspend operator fun invoke(roomId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            if (roomId.isEmpty()) {
                throw RoomError.RoomNotFound()
            }

            // Listen to room updates and update the session's current room
            roomRepository.listenRoomUpdates(roomId)
                .map { result -> result.getOrThrow() }
                .collect { room ->
                    session.setCurrentRoom(room)
                }
        }
        .onFailure { e ->
            Timber.e(e, "ListenRoomUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is RoomError) throw throwable
            else if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}