package com.example.seabattle.domain.usecase.room

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class WaitRoomUseCase(
    val roomRepository: RoomRepository,
    val gameRepository: GameRepository,
    val securePrefs: SecurePrefsData,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    // This function waits for the room to be updated and performs actions based on the room state.
    // It checks the player's ID and updates the room state accordingly.

    suspend operator fun invoke(roomId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val playerId = securePrefs.getUid()
            val flowCollector = roomRepository.getRoomUpdate(roomId)
            .map { result -> result.getOrThrow() }
            .first { room ->
                session.setCurrentRoom(room)
                when (playerId) {
                    room.player1.userId -> {
                        if (room.player2 != null) {
                            if (room.roomState == RoomState.SECOND_PLAYER_JOINED.name) {
                                val newData = mapOf(
                                    "roomState" to RoomState.CREATING_GAME.name,
                                )
                                roomRepository.updateRoom(roomId, newData).getOrThrow()

                            } else if (room.roomState == RoomState.CREATING_GAME.name) {
                                if (session.getCurrentGame() == null) {
                                    val game = Game(
                                        gameId = UUID.randomUUID().toString(),
                                        player1 = room.player1,
                                        player2 = room.player2,
                                        gameState = GameState.INITIAL.name,
                                    )

                                    gameRepository.createGame(game).getOrThrow()
                                    session.setCurrentGame(game)

                                    val newData = mapOf(
                                        "roomState" to RoomState.GAME_CREATED.name,
                                        "gameId" to game.gameId,
                                    )
                                    roomRepository.updateRoom(roomId, newData).getOrThrow()
                                }
                            } else if (room.roomState == RoomState.GAME_STARTED.name) {
                                session.setCurrentRoom(room)
                                return@first true
                            }
                        }
                        return@first false
                    }

                    room.player2?.userId -> {
                        if (room.roomState == RoomState.GAME_CREATED.name) {
                            if (room.gameId == null) {
                                throw IllegalStateException("Game ID is null")
                            }

                            val game = gameRepository.getGame(room.gameId).getOrThrow()
                            session.setCurrentGame(game)

                            val newData = mapOf(
                                "roomState" to RoomState.GAME_STARTED.name
                            )
                            roomRepository.updateRoom(roomId, newData).getOrThrow()

                        } else if (room.roomState == RoomState.GAME_STARTED.name) {
                            session.setCurrentRoom(room)
                            return@first true
                        }
                        return@first false
                    }
                    else -> throw IllegalStateException("User doesn't belong to this room")
                }
            }
        }
    }
}