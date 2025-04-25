package com.example.seabattle.domain.game.usecases

import com.example.seabattle.domain.game.BoardManager
import com.example.seabattle.data.GameManager
import com.example.seabattle.domain.model.GameBoard

class CreateGameUseCase(
    val boardManager: BoardManager,
    val gameManager: GameManager
) {
    suspend operator fun invoke() {
        gameManager.createGame()
    }

    // TO DO: THIS FUNCTION SHOULD BE IN ANOTHER USE CASE
    fun getGameBoard(): GameBoard {
        return boardManager.getGameBoard()
    }
}