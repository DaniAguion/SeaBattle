package com.example.seabattle.data.firestore

import android.util.Log
import com.example.seabattle.data.firestore.entities.GameEntity
import com.example.seabattle.data.firestore.entities.UserProfileEntity
import com.example.seabattle.data.firestore.mappers.toDomainModel
import com.example.seabattle.data.firestore.mappers.toEntity
import com.example.seabattle.domain.firestore.FirestoreRepository
import com.example.seabattle.domain.model.Game
import com.example.seabattle.domain.model.UserProfile
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepositoryImpl(
    private val db: FirebaseFirestore
) : FirestoreRepository {

    override suspend fun createUserProfile(userProfile: UserProfile): Boolean {
        return try {
            val userProfileEntity = userProfile.toEntity()
            db.collection("users").document(userProfileEntity.userId).set(userProfileEntity).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error creating user profile: ${e.message}")
            false
        }
    }

    override suspend fun getUserProfile(userId: String): UserProfile? {
        try {
            val document = db.collection("users").document(userId).get().await()
            if (document.exists()) {
                val userProfile = document.toObject(UserProfileEntity::class.java)
                if (userProfile != null) {
                    return userProfile.toDomainModel()
                }
            }
            return null
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting user profile: ${e.message}")
            return null
        }
    }


    override suspend fun createGame(game: Game): Boolean {
        return try {
            val gameEntity = game.toEntity()
            db.collection("games").document(gameEntity.gameId).set(gameEntity).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error creating game: ${e.message}")
            false
        }
    }


    override suspend fun getGame(gameId: String): Game? {
        try {
            val document = db.collection("games").document(gameId).get().await()
            if (document.exists()) {
                val gameEntity = document.toObject(GameEntity::class.java)
                if (gameEntity != null) {
                    return gameEntity.toDomainModel()
                }
            }
            return null
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting game: ${e.message}")
            return null
        }
    }


    override suspend fun updateGame(game: Game): Boolean {
        return try {
            val document = db.collection("games").document(game.gameId).get().await()
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
            Log.e("FirestoreRepository", "Error updating game: ${e.message}")
            true
        }
    }
}