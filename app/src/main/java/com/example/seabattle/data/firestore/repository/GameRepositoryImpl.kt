package com.example.seabattle.data.firestore.repository

import com.example.seabattle.data.firestore.dto.GameDto
import com.example.seabattle.data.firestore.mappers.toGameDto
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
                    val gameEntity = try {
                        snapshot.toObject(GameDto::class.java)?.toGameEntity()
                    } catch (e: Exception) {
                        Timber.e("Error converting game data: ${e.message}")
                        trySend(Result.failure(e))
                        return@addSnapshotListener
                    }
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

                if (gameDto.player1Ready || gameDto.player2Ready) {
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



    // Function to update a game data. If the game data has changed since the last fetch, it will throw an exception.
    override suspend fun updateGame(game: Game, updatedGame: Game) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction { transaction ->
                val oldGameDto = game.toGameDto()
                val updatedGameDto = updatedGame.toGameDto()

                val gameId = game.gameId
                val document = transaction.get(gamesCollection.document(gameId))

                if (!document.exists()) {
                    throw Exception("Game not found")
                }

                val fetchedGame = document.toObject(GameDto::class.java)
                    ?: throw Exception("Game data is corrupted")

                // Validate original game data
                if (oldGameDto != fetchedGame) {
                    throw Exception("Original game data does not match the current data in the database")
                }

                // Load the new calculated game data and refresh updatedAt field
                transaction.set(gamesCollection.document(gameId), updatedGameDto)
                transaction.update(gamesCollection.document(gameId), mapOf(
                    "updatedAt" to FieldValue.serverTimestamp()
                ))
                return@runTransaction
            }.await()
        }
    }



    // Function to delete a game by gameId
    override suspend fun deleteGame(gameId: String) : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            gamesCollection.document(gameId).delete().await()
            return@runCatching Unit
        }
    }
}