package com.example.seabattle.data.functions

import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.repository.BackendRepository
import com.google.firebase.Firebase
import com.google.firebase.functions.functions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber


class BackendRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher
) : BackendRepository {
    private val functions = Firebase.functions("europe-west1")


    // Function called to finish a game by its ID
    override suspend fun finishGame(gameId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val data = hashMapOf(
                "gameId" to gameId,
            )

            val result = functions
                .getHttpsCallable("finishGame")
                .call(data)
                .await()


            val response = result.data as? Map<*, *>
            val status = response?.get("status") as? String
            val message = response?.get("message") as? String


            when (status) {
                "success" -> {
                    Timber.i("Cloud Function finishGame executed successfully.")
                }
                "already-executed" -> {
                    Timber.i("Cloud Function finishGame was previously executed.")
                }
                else -> {
                    Timber.e(message ?: "Cloud Function finishGame failed.")
                    throw DomainError.Unknown()
                }
            }
        }
    }
}