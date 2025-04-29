package com.example.seabattle.domain.firestore

import com.example.seabattle.domain.model.Game
import com.example.seabattle.domain.model.Room
import com.example.seabattle.domain.model.User
import com.example.seabattle.domain.model.UserBasic
import java.util.UUID

interface FirestoreRepository {
    suspend fun createUser(user: User): Boolean
    suspend fun getUser(userId: String): User?

    suspend fun fetchRooms(): Result<List<Room>>
    suspend fun createRoom(room: Room): Result<Unit>
    suspend fun updateRoom(room: Room): Result<Room?>
    suspend fun deleteRoom(roomId: String): Result<Unit>

    suspend fun createGame(game: Game): Boolean
    suspend fun joinGame(gameId: String, player2: UserBasic, gameState: String): Boolean

    suspend fun getGame(gameId: String): Game?
    suspend fun updateGame(game: Game): Boolean
}