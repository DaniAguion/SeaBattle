package com.example.seabattle.data.functions

import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.repository.ScoreRepository
import com.google.firebase.Firebase
import com.google.firebase.functions.functions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber


class ScoreRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher
) : ScoreRepository {
    private val functions = Firebase.functions("europe-west1")

    override suspend fun updateScore(gameId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val data = hashMapOf(
                "gameId" to gameId,
            )

            val result = functions
                .getHttpsCallable("scoreGame")
                .call(data)
                .await()


            val response = result.data as? Map<*, *>
            val status = response?.get("status") as? String


            when (status) {
                "score-updated" -> {
                    Timber.i("Cloud Function scoreGame successfully. Score updated.")
                }
                "score-unchanged" -> {
                    Timber.i("Cloud Function scoreGame successfully. Score was already updated.")
                }
                else -> {
                    Timber.e("Cloud Function scoreGame failed.")
                    throw DomainError.Unknown()
                }
            }
        }
    }
}