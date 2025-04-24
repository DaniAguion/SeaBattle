package com.example.seabattle.domain.firestore.repository

import com.example.seabattle.domain.model.Game
import com.example.seabattle.domain.model.UserProfile

interface FirestoreRepository {
    suspend fun createUserProfile(userId: String, userProfile: UserProfile): Boolean
    suspend fun getUserProfile(userId: String): UserProfile?
    suspend fun createGame(game: Game): Boolean
    suspend fun getGame(gameId: String): Game?
    suspend fun updateGame(game: Game): Boolean
}