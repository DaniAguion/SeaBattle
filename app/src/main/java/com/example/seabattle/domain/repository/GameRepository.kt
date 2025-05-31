package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun listenGameUpdates(gameId: String) : Flow<Result<Game>>
    suspend fun confirmReady(gameId: String, userId: String) : Result<Unit>
    suspend fun getGame(gameId: String) : Result<Game>
    suspend fun updateGame(oldGame: Game, newGame: Game) : Result<Unit>
    suspend fun leaveGame(gameId: String, userId: String) : Result<Unit>
}