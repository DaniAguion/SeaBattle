package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun listenGameUpdates(gameId: String) : Flow<Result<Game>>
    suspend fun updateUserReady(gameId: String, userId: String) : Result<Unit>
    suspend fun getGame(gameId: String) : Result<Game>
    suspend fun createGame(game: Game) : Result<Unit>
    suspend fun updateGame(gameId: String, newData: Map<String, Any>) : Result<Unit>
}