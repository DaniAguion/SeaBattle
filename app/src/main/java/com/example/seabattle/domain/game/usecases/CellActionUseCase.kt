package com.example.seabattle.domain.game.usecases

import com.example.seabattle.domain.game.repository.GameRepository

class CellActionUseCase(
    val gameRepository: GameRepository
) {
    operator fun invoke(x: Int, y: Int) {
        gameRepository.cellAction(x, y)
    }
}