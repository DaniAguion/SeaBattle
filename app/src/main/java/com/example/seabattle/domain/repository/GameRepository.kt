package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.UserBasic
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGameUpdate(gameId: String) : Flow<Result<Game>>
    suspend fun getGame(gameId: String) : Result<Game>
    suspend fun createGame(game: Game) : Result<Unit>
    suspend fun updateGame(gameId: String, newData: Map<String, Any>) : Result<Unit>
}