package com.example.seabattle.domain.game.usecases

import com.example.seabattle.data.BoardManager
import com.example.seabattle.domain.model.GameBoard

class StartGameUseCase(
    val boardManager: BoardManager
) {
    operator fun invoke() : GameBoard {
        return boardManager.getGameBoard()
    }
}