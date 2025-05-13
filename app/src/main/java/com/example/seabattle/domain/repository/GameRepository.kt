package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.UserBasic

interface GameRepository {
    suspend fun createGame(game: Game) : Result<Unit>
    suspend fun getGame(gameId: String) : Result<Game>
    suspend fun updateGame(gameId: String, newData: Map<String, Any>) : Result<Unit>
}