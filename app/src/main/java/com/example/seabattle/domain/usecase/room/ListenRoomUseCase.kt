package com.example.seabattle.domain.usecase.room


import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.RoomError
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.catch
import timber.log.Timber

class ListenRoomUseCase(
    val roomRepository: RoomRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    // This function listen the room entity and updates the local data
    operator fun invoke(roomId: String): Flow<Result<Room?>> {
        return roomRepository.listenRoomUpdates(roomId)
            .onEach { result ->
                result.onSuccess { room ->
                    if (room != null) {
                        session.setCurrentRoom(room)
                    }
                }
                result.onFailure { throwable ->
                    Timber.e(throwable, "ListenRoomUpdates failed.")
                }
            }
            .catch { throwable ->
                Timber.e(throwable, "ListenRoomUseCase failed.")
                when (throwable) {
                    is RoomError -> emit(Result.failure(throwable))
                    else -> emit(Result.failure(DomainError.Unknown(throwable)))
                }
            }
    }
}