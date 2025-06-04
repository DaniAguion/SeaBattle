package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Room
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun listenGameUpdates(gameId: String) : Flow<Result<Game>>
    suspend fun createGame(roomId: String, logicFunction: (Room) -> Game, updatedRoomData: Map<String, Any>) : Result<String>
    suspend fun getGame(gameId: String) : Result<Game>
    suspend fun updateGameFields(gameId: String, logicFunction: (Game) -> Map<String, Any?>): Result<Unit>
    suspend fun deleteGame(gameId: String) : Result<Unit>
}