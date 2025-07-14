package com.example.seabattle.data.firestore.repository

import com.example.seabattle.data.firestore.dto.UserGamesDto
import com.example.seabattle.data.firestore.errors.toDataError
import com.example.seabattle.data.firestore.mappers.toEntity
import com.example.seabattle.domain.entity.UserGames
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.UserGamesRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserGamesRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : UserGamesRepository {
    private val userGamesCollection = db.collection("userGames")


    // Function to create user games
    override suspend fun createUserGames(userId: String) : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userGamesDto = UserGamesDto(userId = userId)
            userGamesCollection.document(userId).set(userGamesDto).await()
        }
        .map { _ -> }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    // Function to get user games by user ID
    override suspend fun getUserGames(userId: String) : Result<UserGames> = withContext(ioDispatcher) {
        runCatching {
            val document = userGamesCollection.document(userId).get().await()
            if (document.exists()) {
                val userGamesEntity = document.toObject(UserGamesDto::class.java)?.toEntity()
                return@runCatching userGamesEntity ?: throw UserError.UserGamesNotFound()
            } else {
                throw UserError.UserGamesNotFound()
            }
        }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    // Function to delete user games
    override suspend fun deleteUserGames(userId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            userGamesCollection.document(userId).delete().await()
        }
        .map { _ -> }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    // Function to update the current game ID for a user
    override suspend fun updateCurrentGameId(userId: String, gameId: String?): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            userGamesCollection.document(userId).update("currentGameId", gameId).await()
        }
        .map { _ -> }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    // Function to update the invited game ID for a user
    override suspend fun updateInvitedGameId(userId: String, gameId: String?): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            userGamesCollection.document(userId).update("invitedGameId", gameId).await()
        }
        .map { _ -> }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }
}