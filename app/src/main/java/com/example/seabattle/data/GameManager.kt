package com.example.seabattle.data

import com.example.seabattle.domain.firestore.repository.FirestoreRepository
import com.example.seabattle.domain.model.Game
import com.google.firebase.firestore.FieldValue
import java.util.UUID

class GameManager(
    private val fireStoreRepository: FirestoreRepository,
    private val boardManager: BoardManager,
    private val sessionManager: SessionManager
) {
    suspend fun createGame(): Boolean {
        val player1 = sessionManager.getUserProfile()

        val game = Game(
            gameId = UUID.randomUUID().toString(),
            player1 = player1,
            player1Board = boardManager.getGameBoard().toMapOfMaps(),
            player2 = sessionManager.getUserProfile(),
            player2Board = boardManager.getGameBoard().toMapOfMaps(),
            currentTurn = 0,
            currentPlayer = 0,
            createdAt = FieldValue.serverTimestamp(),
            updatedAt = FieldValue.serverTimestamp(),
            gameFinished = false,
            winnerId = null,
        )
        return fireStoreRepository.createGame(game)
    }
}