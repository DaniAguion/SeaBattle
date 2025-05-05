package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.services.BoardManager
import com.example.seabattle.data.GameManager
import com.example.seabattle.domain.entity.GameBoard

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