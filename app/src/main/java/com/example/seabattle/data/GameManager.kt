package com.example.seabattle.data

import com.example.seabattle.data.firestore.entities.GameEntity
import com.example.seabattle.domain.auth.SessionManager
import com.example.seabattle.domain.firestore.FirestoreRepository
import com.example.seabattle.domain.game.BoardManager
import com.example.seabattle.domain.model.Game
import com.google.firebase.firestore.FieldValue
import java.util.UUID

class GameManager(
    private val fireStoreRepository: FirestoreRepository,
    private val boardManager: BoardManager,
    private val sessionManager: SessionManager
) {
    suspend fun createGame(): Boolean {
        val player = sessionManager.getFireStoreUserProfile() ?: return false

        val game = Game(
            gameId = UUID.randomUUID().toString(),
            player1 = player,
            player1Board = boardManager.getGameBoard().toMapOfMaps(),
            player2 = player,
            player2Board = boardManager.getGameBoard().toMapOfMaps(),
            currentTurn = 1,
            currentPlayer = 1,
            gameFinished = false,
            winnerId = null,
        )
        return fireStoreRepository.createGame(game)
    }
}