package com.example.seabattle.data.firestore.repository

import android.util.Log
import com.example.seabattle.data.firestore.dto.GameDtoRd
import com.example.seabattle.data.firestore.mappers.toGameDto
import com.example.seabattle.data.firestore.mappers.toGameEntity
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.repository.GameRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenSource
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.SnapshotListenOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.onFailure

class GameRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : GameRepository {
    private val gamesCollection = db.collection("games")
    private val tag = "GameRepository"



    override fun listenGameUpdates(gameId: String) : Flow<Result<Game>>
    = callbackFlow {
        val options = SnapshotListenOptions.Builder()
            .setMetadataChanges(MetadataChanges.INCLUDE)
            .build()

        val listener = gamesCollection
            .document(gameId)
            .addSnapshotListener(options) { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot == null || !snapshot.exists()) {
                    trySend(Result.failure(Exception("Game not found")))
                    return@addSnapshotListener
                }
                if (!snapshot.metadata.isFromCache()) {
                    val gameEntity = snapshot.toObject(GameDtoRd::class.java)?.toGameEntity()
                    if (gameEntity == null) {
                        trySend(Result.failure(Exception("Game not found")))
                        return@addSnapshotListener
                    }
                    trySend(Result.success(gameEntity))
                }
            }
        awaitClose { listener.remove() }
    }.flowOn(ioDispatcher)



    override suspend fun updateUserReady(gameId: String, userId: String) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction { transaction ->
                val document = gamesCollection.document(gameId)
                val snapshot = transaction.get(document)

                if (!snapshot.exists()) {
                    throw Exception("Game not found")
                }

                val gameDto = snapshot.toObject(GameDtoRd::class.java)
                    ?: throw Exception("Game data is corrupted")

                when (userId) {
                    gameDto.player1.userId -> {
                        gameDto.player1Ready = true
                    }
                    gameDto.player2.userId -> {
                        gameDto.player2Ready = true
                    }
                    else -> throw Exception("User does not belong to this game")
                }



                if (gameDto.player1Ready && gameDto.player2Ready) {
                    gameDto.gameState = GameState.IN_PROGRESS.name
                }

                transaction.set(document, gameDto)
                return@runTransaction Unit
            }.await()
        }
    }



    override suspend fun getGame(gameId: String): Result<Game> = withContext(ioDispatcher) {
        runCatching {
            val document = gamesCollection.document(gameId).get().await()
            if (document.exists()) {
                val gameEntity = document.toObject(GameDtoRd::class.java)?.toGameEntity()
                if (gameEntity == null) {
                    throw Exception("Game not found")
                }
                return@runCatching gameEntity
            } else {
                throw Exception("Document not found")
            }
        }
        .onFailure { e ->
            Log.e(tag, "Error fetching game: ${e.message}")
            emptyList<Game>()
        }
    }



    override suspend fun createGame(game: Game) : Result<Unit>  = withContext(ioDispatcher) {
        runCatching {
            val gameDto = game.toGameDto()
            gamesCollection.document(gameDto.gameId)
                .set(gameDto)
                .await()
        }
        .map { _ -> }
        .onFailure { e ->
            Log.e(tag, "Error creating game with Id: ${game.gameId}. ${e.message}")
        }
    }



    override suspend fun updateGame(gameId: String, newData: Map<String, Any>) : Result<Unit>
            = withContext(ioDispatcher) {
        runCatching {
            val document = gamesCollection.document(gameId).get().await()
            if (document.exists()) {
                val updatedGame = newData + mapOf(
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                gamesCollection.document(gameId).update(updatedGame).await()
            } else {
                throw Exception("Game not found")
            }
        }
        .map { _ -> }
        .onFailure { e ->
            Log.e(tag, "Error updating game: ${gameId}. ${e.message}")
        }
    }
}