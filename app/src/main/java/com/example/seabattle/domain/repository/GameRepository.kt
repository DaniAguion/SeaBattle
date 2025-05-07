package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.UserBasic

interface GameRepository {
    suspend fun createGame(game: Game) : Result<Unit>
    suspend fun joinGame(gameId: String, player2: UserBasic, gameState: String) : Boolean
    suspend fun getGame(gameId: String) : Game?
    suspend fun updateGame(game: Game) : Boolean
}