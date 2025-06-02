package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.entity.toBasic
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.RoomError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.RoomRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
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

            // Create the room in the repository
            val roomId = UUID.randomUUID().toString()

            var room = Room(
                roomId = roomId,
                roomName = roomName,
                roomState = RoomState.WAITING_FOR_PLAYER.name,
                player1 = user.toBasic(),
            )

            roomRepository.createRoom(room).getOrThrow()

            // Fetch the updated room and set it in the session
            room = roomRepository.getRoom(roomId).getOrThrow()
            session.setCurrentRoom(room)
            return@runCatching
        }
        .onFailure { e ->
            Timber.e(e, "CreateRoomUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is RoomError) throw throwable
            else if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}