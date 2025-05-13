package com.example.seabattle.domain.usecase.game

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class UserReadyUseCase(
    val gameRepository: GameRepository,
    val securePrefs: SecurePrefsData,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    suspend operator fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val playerId = securePrefs.getUid()
            val game = session.getCurrentGame() ?: throw Exception("Game not found")
            var newData : Map<String, Any>

            if (game.player1.userId == playerId) {
                newData = mapOf(
                    "player1Ready" to true,
                )
                if (game.player2Ready) {
                    newData = newData + mapOf(
                        "gameState" to GameState.IN_PROGRESS.name,
                    )
                }
            } else if (game.player2.userId == playerId) {
                newData = mapOf(
                    "player2Ready" to true,
                )
                if (game.player1Ready) {
                    newData = newData + mapOf(
                        "gameState" to GameState.IN_PROGRESS.name,
                    )
                }
            } else {
                throw Exception("Player doesn't belong to this game")
            }

            gameRepository.updateGame(game.gameId, newData).getOrThrow()
            val newGame = gameRepository.getGame(game.gameId).getOrThrow()
            return@runCatching session.setCurrentGame(newGame)
        }
    }
}