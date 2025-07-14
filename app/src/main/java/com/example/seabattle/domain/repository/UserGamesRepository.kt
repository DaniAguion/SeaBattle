package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.UserGames


interface UserGamesRepository {
    suspend fun createUserGames(userId: String) : Result<Unit>
    suspend fun getUserGames(userId: String) : Result<UserGames>
    suspend fun deleteUserGames(userId: String): Result<Unit>
    suspend fun updateCurrentGameId(userId: String, gameId: String?): Result<Unit>
    suspend fun updateInvitedGameId(userId: String, gameId: String?): Result<Unit>
}