package com.example.seabattle.domain.firestore

import com.example.seabattle.domain.model.Game
import com.example.seabattle.domain.model.User

interface FirestoreRepository {
    suspend fun createUser(user: User): Boolean
    suspend fun getUser(userId: String): User?

    suspend fun createGame(game: Game): Boolean
    suspend fun getGame(gameId: String): Game?
    suspend fun updateGame(game: Game): Boolean
}