package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.repository.GameRepository


class MakeMoveUseCase(
    val gameRepository: GameRepository,
    val session: Session,
) {
    suspend operator fun invoke(x: Int, y: Int) : Unit {
        val userId = session.getCurrentUserId()
        val gameId = session.getCurrentGameId()
        val game = session.getCurrentGame()

        if (userId.isEmpty() || gameId.isEmpty() || game == null) {
            throw IllegalStateException("User is not logged in or game is not set")
        }

        //
        /// VALIDATE DATA OF GAME TO MAKE A MOVE
        //
        if (game.gameState != GameState.IN_PROGRESS.name) {
            throw Exception("Game is not in progress state")
        }
        if (game.currentPlayer != userId) {
            throw Exception("It's not user turn")
        }

        val gameBoard = if (game.currentPlayer == game.player1.userId){
            game.boardForPlayer1.mapValues { (_, innerMap) -> innerMap.toMutableMap() }.toMutableMap()
        } else if(game.currentPlayer == game.player2.userId){
            game.boardForPlayer2.mapValues { (_, innerMap) -> innerMap.toMutableMap() }.toMutableMap()
        } else { throw Exception("Invalid player") }

        // Check valid cell coordinates
        val row = gameBoard[x.toString()] ?: throw Exception("Invalid cell coordinates")
        val cellValue = row[y.toString()] ?: throw Exception("Invalid cell coordinates")

        if (gameBoard[x.toString()] == null || gameBoard[x.toString()]?.get(y.toString()) == null) {
            throw Exception("Invalid cell coordinates")
        }

        when (cellValue) {
            0 -> gameBoard[x.toString()]?.put(y.toString(), 2)
            1 -> gameBoard[x.toString()]?.put(y.toString(), 3)
            else -> {
                throw Exception("Invalid cell value")
            }
        }

        // Copy game to avoid modifying the original game boards
        val newGame = game.copy(
            boardForPlayer1 = if (game.currentPlayer == game.player1.userId) gameBoard else game.boardForPlayer1,
            boardForPlayer2 = if (game.currentPlayer == game.player2.userId) gameBoard else game.boardForPlayer2,
            currentTurn = game.currentTurn + 1,
            currentPlayer = if (game.currentPlayer == game.player1.userId) game.player2.userId else game.player1.userId
        )

        gameRepository.updateGame(oldGame= game, newGame= newGame).getOrThrow()
    }
}