package com.example.seabattle.domain.usecase.room

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WaitRoomUseCase(
    val roomRepository: RoomRepository,
    val securePrefs: SecurePrefsData,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(roomId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val playerId = securePrefs.getUid()
            val flowCollector = roomRepository.getRoomUpdate(roomId)
            .map { result -> result.getOrThrow() }
            .first { room ->
                when (playerId) {
                    room.player1.userId -> {
                        if ((room.player2 != null) && (room.roomState == RoomState.SECOND_PLAYER_JOINED.name)) {
                            val newData = mapOf(
                                "roomState" to RoomState.STARTING_GAME.name
                            )
                            roomRepository.updateRoom(roomId, newData).getOrThrow()
                            true
                        } else {
                            false
                        }
                    }

                    room.player2?.userId -> {
                        if (room.roomState == RoomState.STARTING_GAME.name) {
                            roomRepository.deleteRoom(roomId).getOrThrow()
                            true
                        } else {
                            false
                        }
                    }

                    else -> throw IllegalStateException("User doesn't belong to this room")
                }
            }
        }
    }
}