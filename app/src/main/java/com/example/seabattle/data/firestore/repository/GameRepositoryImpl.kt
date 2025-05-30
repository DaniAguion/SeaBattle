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



    override suspend fun makeMove(gameId: String, userId: String, x: Int, y: Int) : Result<Unit>
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

                if (gameDto.gameState != GameState.IN_PROGRESS.name) {
                    throw Exception("Game is not in progress state")
                }

                if (gameDto.currentPlayer != userId) {
                    throw Exception("It's not user turn")
                }

                val gameBoard = if (gameDto.currentPlayer == gameDto.player1.userId){
                    gameDto.boardForPlayer1
                } else if(gameDto.currentPlayer == gameDto.player2.userId){
                    gameDto.boardForPlayer2
                } else { throw Exception("Player cannot hit own ship") }

                val row = gameBoard[x.toString()]?.toMutableMap() ?: throw Exception("Invalid cell coordinates")
                val cellValue = row[y.toString()] ?: throw Exception("Invalid cell coordinates")

                if (gameBoard[x.toString()] == null || gameBoard[x.toString()]?.get(y.toString()) == null) {
                    throw Exception("Invalid cell coordinates")
                }

                when (cellValue) {
                    0 -> gameBoard[x.toString()]?.put(y.toString(), 2)
                    1 -> gameBoard[x.toString()]?.put(y.toString(), 3)
                    else -> {
                        throw Exception("Invalid cell")
                        Timber.e("Invalid cell value: ${gameBoard[x.toString()]?.get(y.toString())}")
                    }
                }

                if (gameDto.currentPlayer == gameDto.player1.userId) {
                    transaction.update(document, mapOf(
                        "boardForPlayer1" to gameBoard,
                        "currentTurn" to FieldValue.increment(1),
                        "currentPlayer" to gameDto.player2.userId, // switch to player 2
                        "updatedAt" to FieldValue.serverTimestamp()
                    ))
                } else {
                    transaction.update(document, mapOf(
                        "boardForPlayer2" to gameBoard,
                        "currentTurn" to FieldValue.increment(1),
                        "currentPlayer" to gameDto.player1.userId, // switch to player 1
                        "updatedAt" to FieldValue.serverTimestamp()
                    ))
                }


                return@runTransaction Unit
            }.await()
        }
    }
}