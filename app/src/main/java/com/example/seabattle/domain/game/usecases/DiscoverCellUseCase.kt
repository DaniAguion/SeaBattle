package com.example.seabattle.domain.game.usecases

import com.example.seabattle.data.BoardManager
import com.example.seabattle.domain.model.GameBoard

class CellActionUseCase(
    val boardManager: BoardManager
) {
    operator fun invoke(x: Int, y: Int) : GameBoard {
        return boardManager.discoverCell(x, y)
    }
}