package com.example.seabattle.data.firestore.repository

import android.util.Log
import com.example.seabattle.data.firestore.dto.GameDtoRd
import com.example.seabattle.data.firestore.mappers.toDto
import com.example.seabattle.data.firestore.mappers.toEntity
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.UserBasic
import com.example.seabattle.domain.repository.GameRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GameRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : GameRepository {
    private val gamesCollection = db.collection("games")
    private val tag = "GameRepository"

    override suspend fun createGame(game: Game) : Result<Unit>  = withContext(ioDispatcher) {
        runCatching {
            val gameDto = game.toDto()
            gamesCollection.document(gameDto.gameId)
                .set(gameDto)
                .await()
        }
        .map { _ -> }
        .onFailure { e ->
            Log.e(tag, "Error creating game with Id: ${game.gameId}. ${e.message}")
        }
    }


    override suspend fun joinGame(gameId: String, player2: UserBasic, gameState: String): Boolean {
        return try {
            val document = gamesCollection.document(gameId).get().await()
            if (document.exists()) {
                val newData = mapOf(
                    "player2" to player2,
                    "gameState" to gameState
                )
                gamesCollection.document(gameId).update(newData).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e(tag, "Error updating game: ${e.message}")
            true
        }
    }


    override suspend fun getGame(gameId: String): Game? {
        try {
            val document = gamesCollection.document(gameId).get().await()
            if (document.exists()) {
                val gameDto = document.toObject(GameDtoRd::class.java)
                if (gameDto != null) {
                    return gameDto.toEntity()
                }
            }
            return null
        } catch (e: Exception) {
            Log.e(tag, "Error getting game: ${e.message}")
            return null
        }
    }


    override suspend fun updateGame(game: Game): Boolean {
        return try {
            val document = gamesCollection.document(game.gameId).get().await()
            if (document.exists()) {
                val newData = mapOf(
                    "player1Board" to game.player1Board,
                    "player2Board" to game.player2Board,
                    "actualTurn" to game.currentTurn,
                    "currentPlayer" to game.currentPlayer,
                    "updatedAt" to FieldValue.serverTimestamp(),
                    "gameFinished" to game.gameFinished,
                    "winner" to (game.winnerId ?: ""),
                )
                db.collection("games").document(game.gameId).update(newData).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e(tag, "Error updating game: ${e.message}")
            true
        }
    }
}