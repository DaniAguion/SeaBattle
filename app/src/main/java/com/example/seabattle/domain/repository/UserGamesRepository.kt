package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.domain.entity.UserGames
import kotlinx.coroutines.flow.Flow


interface UserGamesRepository {
    suspend fun createUserGames(userId: String) : Result<Unit>
    fun listenToUserGames(userId: String): Flow<Result<UserGames>>
    suspend fun deleteUserGames(userId: String): Result<Unit>
    suspend fun updateCurrentGameId(userId: String, gameId: String?): Result<Unit>
    suspend fun inviteToGame(guestId: String, invitation: Invitation): Result<Unit>
    suspend fun deleteInvitation(userId: String): Result<Unit>
}