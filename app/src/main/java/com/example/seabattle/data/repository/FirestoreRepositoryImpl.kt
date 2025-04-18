package com.example.seabattle.data.repository

import android.util.Log
import com.example.seabattle.domain.firestore.repository.FirestoreRepository
import com.example.seabattle.domain.model.Game
import com.example.seabattle.domain.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepositoryImpl(
    private val db: FirebaseFirestore
) : FirestoreRepository {
    override suspend fun createUserProfile(userId: String, userProfile: UserProfile): Boolean {
        return try {
            db.collection("users").document(userId).set(userProfile).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error creating user profile: ${e.message}")
            false
        }
    }

    override suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val document = db.collection("users").document(userId).get().await()
            if (document.exists()) {
                document.toObject(UserProfile::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting user profile: ${e.message}")
            null
        }
    }


    override suspend fun createGame(game: Game): Boolean {
        return try {
            db.collection("games").document().set(game).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error creating game: ${e.message}")
            false
        }
    }


    override suspend fun getGame(gameId: String): Game? {
        return try {
            val document = db.collection("games").document(gameId).get().await()
            if (document.exists()) {
                document.toObject(Game::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting game: ${e.message}")
            null
        }
    }


    override suspend fun updateGame(gameId: String, game: Game): Boolean {
        return try {
            val document = db.collection("games").document(gameId).get().await()
            if (document.exists()) {
                val newData = mapOf(
                    "player1BattlePlan" to game.player1BattlePlan,
                    "player1Board" to game.player1Board,
                    "player2BattlePlan" to game.player2Board,
                    "actualTurn" to game.actualTurn,
                    "actualPlayer" to game.actualPlayer,
                    "turnTimer" to game.turnTimer,
                    "gameStatus" to game.gameStatus,
                    "winner" to game.winner
                )
                db.collection("games").document(gameId).update(newData).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error updating game: ${e.message}")
            true
        }
    }
}