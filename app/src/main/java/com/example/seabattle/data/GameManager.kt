package com.example.seabattle.data

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.toBasic
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.UserRepository
import java.util.UUID

class GameManager(
    private val userRepository: UserRepository,
    private val securePrefs: SecurePrefsData,
    private val gameRepository: GameRepository
) {
    suspend fun createGame(): Boolean {
        /*
        val userId = securePrefs.getUid()
        val player1 = userRepository.getUser(userId) ?: return false

        val game = Game(
            gameId = UUID.randomUUID().toString(),
            player1 = player1.toBasic(),
            currentTurn = 0,
            gameFinished = false,
            gameState = GameState.WAITING_FOR_PLAYER.name
        )
        return gameRepository.createGame(game)
         */
        return false
    }


    suspend fun joinGame(gameId: String): Boolean {
        /*
        val player2 = sessionManager.getFireStoreUserProfile()?.toBasic() ?: return false
        return gameRepository.joinGame(
            gameId = gameId,
            player2 = player2,
            gameState = GameState.CHECK_READY.name
        )
         */
        return false
    }
}