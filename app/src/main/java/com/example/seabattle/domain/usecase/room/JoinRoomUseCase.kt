package com.example.seabattle.domain.usecase.room

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.entity.toBasic
import com.example.seabattle.domain.repository.RoomRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class JoinRoomUseCase(
    val roomRepository: RoomRepository,
    val userRepository: UserRepository,
    val securePrefs: SecurePrefsData,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(roomId: String): Result<Unit>  = withContext(ioDispatcher) {
        runCatching {
            val playerId = securePrefs.getUid()
            val player2 = userRepository.getUser(playerId).getOrThrow()
            if (player2 == null) {
                throw Exception("User not found")
            }
            val newData = mapOf(
                "roomState" to RoomState.SECOND_PLAYER_JOINED.name,
                "numberOfPlayers" to 2,
                "player2" to player2.toBasic(),
            )
            roomRepository.updateRoom(roomId, newData).getOrThrow()
        }
    }
}