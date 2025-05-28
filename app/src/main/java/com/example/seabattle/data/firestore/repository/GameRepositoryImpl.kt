package com.example.seabattle.data.firestore.repository

import com.example.seabattle.data.firestore.dto.GameDto
import com.example.seabattle.data.firestore.mappers.toGameEntity
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.repository.GameRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.SnapshotListenOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.onFailure

class GameRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : GameRepository {
    private val gamesCollection = db.collection("games")



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
                    val gameEntity = snapshot.toObject(GameDto::class.java)?.toGameEntity()
                    if (gameEntity == null) {
                        trySend(Result.failure(Exception("Game not found")))
                        return@addSnapshotListener
                    }
                    trySend(Result.success(gameEntity))
                }
            }
        awaitClose { listener.remove() }
    }.flowOn(ioDispatcher)



    override suspend fun confirmReady(gameId: String, userId: String) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction { transaction ->
                val document = gamesCollection.document(gameId)
                val snapshot = transaction.get(document)

                if (!snapshot.exists()) {
                    throw Exception("Game not found")
                }

                val gameDto = snapshot.toObject(GameDto::class.java)
                    ?: throw Exception("Game data is corrupted")

                var newData: Map<String, Any> = mapOf(
                    "updatedAt" to FieldValue.serverTimestamp()
                )

                when(userId) {
                    gameDto.player1.userId -> {
                        newData = newData + mapOf( "player1Ready" to true )
                    }
                    gameDto.player2.userId -> {
                        newData = newData + mapOf( "player2Ready" to true )
                    }
                    else -> throw Exception("User does not belong to this game")
                }

                if (gameDto.player1Ready && gameDto.player2Ready) {
                    newData = newData + mapOf( "gameState" to GameState.IN_PROGRESS.name )
                }

                transaction.update(document, newData)
                return@runTransaction
            }.await()
        }
    }



    override suspend fun getGame(gameId: String): Result<Game> = withContext(ioDispatcher) {
        runCatching {
            val document = gamesCollection.document(gameId).get().await()
            if (document.exists()) {
                val gameEntity = document.toObject(GameDto::class.java)?.toGameEntity()
                if (gameEntity == null) {
                    throw Exception("Game not found")
                }
                return@runCatching gameEntity
            } else {
                throw Exception("Document not found")
            }
        }
        .onFailure { e ->
            Timber.e("Error fetching game: ${e.message}")
            emptyList<Game>()
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
            Timber.e("Error updating game: ${gameId}. ${e.message}")
        }
    }


    override suspend fun leaveGame(gameId: String, userId: String) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction { transaction ->
                val document = gamesCollection.document(gameId)
                val snapshot = transaction.get(document)

                if (!snapshot.exists()) {
                    throw Exception("Game not found")
                }

                val gameDto = snapshot.toObject(GameDto::class.java)
                    ?: throw Exception("Game data is corrupted")

                // If the game is already finished or aborted, we can just delete it
                // otherwise we update the game state to GAME_ABORTED
                if (gameDto.gameState == GameState.GAME_ABORTED.name ||
                    gameDto.gameState == GameState.GAME_ABANDONED.name)
                {
                    transaction.delete(document)
                } else if(gameDto.gameState == GameState.CHECK_READY.name) {
                    transaction.update(document, mapOf(
                        "updatedAt" to FieldValue.serverTimestamp(),
                        "gameState" to GameState.GAME_ABORTED.name,
                        "gameFinished" to true,
                        "winnerId" to null
                    ))
                }
                else {
                    // TO DO
                }
                return@runTransaction
            }.await()
        }
    }
}