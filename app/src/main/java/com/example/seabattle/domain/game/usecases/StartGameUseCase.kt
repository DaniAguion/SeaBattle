package com.example.seabattle.domain.game.usecases

import com.example.seabattle.data.BoardManager
import com.example.seabattle.data.GameManager
import com.example.seabattle.domain.model.GameBoard

class StartGameUseCase(
    val boardManager: BoardManager,
    val gameManager: GameManager
) {
    suspend operator fun invoke() {
        gameManager.createGame()
    }

    fun getGameBoard(): GameBoard {
        return boardManager.getGameBoard()
    }
}