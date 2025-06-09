package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun fetchGames(userId: String) : Flow<Result<List<Game>>>
    fun listenGameUpdates(gameId: String) : Flow<Result<Game>>
    suspend fun createGame(game: Game) : Result<Unit>
    suspend fun getGame(gameId: String) : Result<Game>
    suspend fun updateGameFields(gameId: String, logicFunction: (Game) -> Map<String, Any?>): Result<Unit>
    suspend fun deleteGame(gameId: String) : Result<Unit>
}