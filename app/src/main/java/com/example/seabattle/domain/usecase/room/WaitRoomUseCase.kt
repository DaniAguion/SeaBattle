package com.example.seabattle.domain.usecase.room

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.entity.toBasic
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class WaitRoomUseCase(
    val roomRepository: RoomRepository,
    val securePrefs: SecurePrefsData,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(roomId: String): Result<Unit>  = withContext(ioDispatcher) {
        runCatching {
            val playerId = securePrefs.getUid()
            roomRepository.getRoomUpdate(roomId)
                .collect { result ->
                result
                    .onSuccess { room ->
                        when (playerId) {
                            room.player1.userId -> {
                                if ((room.player2 != null) && (room.roomState == RoomState.SECOND_PLAYER_JOINED.name)) {
                                    val newData = mapOf(
                                        "roomState" to RoomState.STARTING_GAME.name
                                    )
                                    roomRepository.updateRoom(roomId, newData).getOrThrow()
                                }
                            }

                            room.player2?.userId -> {
                                if (room.roomState == RoomState.STARTING_GAME.name) {
                                    print("Player 2 received room update")
                                }
                            }
                            else -> throw Exception("User doesn't belong to this room")
                        }
                    }
                    .onFailure { throwable ->
                        throw Exception("Error while waiting for room update: ${throwable.message}")
                    }
            }
        }
    }
}