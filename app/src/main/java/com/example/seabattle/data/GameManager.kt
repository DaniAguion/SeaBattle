package com.example.seabattle.data

import com.example.seabattle.domain.auth.SessionManager
import com.example.seabattle.domain.firestore.FirestoreRepository
import com.example.seabattle.domain.game.BoardManager
import com.example.seabattle.domain.model.Game
import com.example.seabattle.domain.model.GameState
import com.example.seabattle.domain.model.toBasic
import java.util.UUID

class GameManager(
    private val fireStoreRepository: FirestoreRepository,
    private val boardManager: BoardManager,
    private val sessionManager: SessionManager
) {
    suspend fun createGame(): Boolean {
        val player1 = sessionManager.getFireStoreUserProfile() ?: return false
        val game = Game(
            gameId = UUID.randomUUID().toString(),
            player1 = player1.toBasic(),
            currentTurn = 0,
            gameFinished = false,
            gameState = GameState.WAITING_FOR_PLAYER.name
        )
        return fireStoreRepository.createGame(game)
    }


    suspend fun joinGame(gameId: String): Boolean {
        val player2 = sessionManager.getFireStoreUserProfile()?.toBasic() ?: return false
        return fireStoreRepository.joinGame(
            gameId = gameId,
            player2 = player2,
            gameState = GameState.CHECK_READY.name
        )
    }
}