package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun listenGameUpdates(gameId: String) : Flow<Result<Game>>
    suspend fun getGame(gameId: String) : Result<Game>
    suspend fun updateGame(game: Game, updatedGame: Game) : Result<Unit>
    suspend fun updateGameField(gameId: String, newData: Map<String, Any>) : Result<Unit>
    suspend fun deleteGame(gameId: String) : Result<Unit>
}