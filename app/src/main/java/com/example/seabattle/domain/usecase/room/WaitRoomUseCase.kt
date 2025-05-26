package com.example.seabattle.domain.usecase.room


import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.UUID


class WaitRoomUseCase(
    val roomRepository: RoomRepository,
    val gameRepository: GameRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    // This function will be executed when the user is in the waiting room, each time room state is updated.
    // Its purpose is to handle the logic to transition from the waiting room to a game.
    suspend operator fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = session.getCurrentUserId()
            val room = session.getCurrentRoom()

            if (room == null){
                throw Exception("Room is not set")
            }

            val roomId = room.roomId

            when(room.roomState) {
                RoomState.WAITING_FOR_PLAYER.name -> {
                    return@runCatching
                }
                RoomState.SECOND_PLAYER_JOINED.name -> {
                    // If the second player has joined the room, the first player will create a game.
                    if (room.player2 == null) {
                        throw Exception("Player 2 is not set")
                    }
                    if (userId == room.player1.userId) {
                        // Create a new game and update room state to GAME_CREATED and attach the gameId to the room.
                        val gameId = UUID.randomUUID().toString()
                        roomRepository.createGame(gameId, roomId).getOrThrow()

                        // Fetch the game and set it in the session.
                        val game = gameRepository.getGame(gameId).getOrThrow()
                        session.setCurrentGame(game)
                    }
                }
                RoomState.GAME_CREATED.name -> {
                    // If the first player has created a game, the second player will join the game.
                    if (room.player2 == null) {
                        throw Exception("Player 2 is not set")
                    }
                    if (userId == room.player2.userId) {
                        val gameId = room.gameId
                        if (gameId.isNullOrEmpty()) {
                            throw Exception("Game ID is not set")
                        }
                        // Update the room state to GAME_STARTED.
                        val game = roomRepository.joinGame(gameId, roomId).getOrThrow()
                        session.setCurrentGame(game)
                    }
                }
                RoomState.GAME_STARTING.name -> {
                    // Delete the room if it wasn't deleted yet and clear the room from the session.
                    val room = roomRepository.getRoom(roomId).getOrNull()
                    if (room != null) {
                        roomRepository.deleteRoom(roomId).getOrThrow()
                    }
                    session.clearCurrentRoom()
                }
            }

        }
    }
}