package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.services.BoardManager
import com.example.seabattle.domain.entity.GameBoard

class DiscoverCellUseCase(
    val boardManager: BoardManager
) {
    operator fun invoke(x: Int, y: Int) : GameBoard {
        return boardManager.discoverCell(x, y)
    }
}