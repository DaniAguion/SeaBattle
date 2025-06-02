package com.example.seabattle.domain.usecase.room


import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.GameBoardRepository
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.UUID


class WaitRoomUseCase(
    val roomRepository: RoomRepository,
    val gameRepository: GameRepository,
    val gameBoardRepository: GameBoardRepository,
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

                // 1st State: If the room is created, the first player will wait for the second player to join.
                RoomState.WAITING_FOR_PLAYER.name -> {
                    return@runCatching
                }


                // 2nd State: If the second player has joined the room, the first player will create a game.
                RoomState.SECOND_PLAYER_JOINED.name -> {
                    if (userId == room.player1.userId) {
                        val gameId = UUID.randomUUID().toString()

                        // Function to create a game with the given room.
                        fun createGame(room: Room): Game {
                            // Validate room state before creating a game.
                            if (room.roomState != RoomState.SECOND_PLAYER_JOINED.name || room.player2 == null) {
                                throw Exception("Room is not available for creating a game")
                            }
                            // Create game board for player 1 and register the ships for player 2.
                            // Create game board for player 2 and register the ships for player 1.
                            gameBoardRepository.createGameBoard().getOrThrow()
                            val boardForPlayer1 = gameBoardRepository.getGameBoard()
                            val player2Ships = gameBoardRepository.getShipList()
                            gameBoardRepository.createGameBoard().getOrThrow()
                            val boardForPlayer2 = gameBoardRepository.getGameBoard()
                            val player1Ships = gameBoardRepository.getShipList()
                            var game = Game(
                                gameId = gameId,
                                player1 = room.player1,
                                boardForPlayer1 = boardForPlayer1,
                                player1Ships = player1Ships,
                                player2 = room.player2,
                                boardForPlayer2 = boardForPlayer2,
                                player2Ships = player2Ships,
                                gameState = GameState.CHECK_READY.name,
                                currentPlayer = listOf(room.player1.userId, room.player2.userId).random(),
                            )
                            return game
                        }

                        // Update the room state to GAME_CREATED and indicate the game ID.
                        val updatedRoomData : Map<String, Any> = mapOf(
                            "roomState" to RoomState.GAME_CREATED.name,
                            "gameId" to gameId,
                        )

                        // Update the room in the database and create a game.
                        gameRepository.createGame(roomId, ::createGame, updatedRoomData).getOrThrow()
                        // Fetch the game and set it in the session.
                        val game = gameRepository.getGame(gameId).getOrThrow()
                        session.setCurrentGame(game)
                    }
                }


                // 3rd State: If the first player has created a game, the second player will join the game.
                RoomState.GAME_CREATED.name -> {
                    if (room.player2 == null || room.gameId == null) {
                        throw Exception("Missing data in the room object")
                    }

                    if (userId == room.player2.userId) {

                        // Function to validate the room state and join the game.
                        fun joinGame(room: Room): Map<String, Any> {
                            if (room.roomState != RoomState.GAME_CREATED.name || room.gameId == null) {
                                throw Exception("Game is not available for joining the game")
                            }
                            return mapOf(
                                "roomState" to RoomState.GAME_STARTING.name
                            )
                        }

                        // Update the room state to GAME_STARTING and join the game.
                        roomRepository.updateRoomFields(roomId, ::joinGame).getOrThrow()
                        // Fetch the game and set it in the session.
                        val game = gameRepository.getGame(room.gameId).getOrThrow()
                        session.setCurrentGame(game)
                    }
                }


                // 4th State: If the game is starting, the room is not needed anymore, so we can delete it.
                RoomState.GAME_STARTING.name, RoomState.ROOM_ABANDONED.name  -> {
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