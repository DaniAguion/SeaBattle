package com.example.seabattle.data.firestore.repository

import com.example.seabattle.data.firestore.dto.GameDto
import com.example.seabattle.data.firestore.mappers.toGameDto
import com.example.seabattle.data.firestore.mappers.toGameEntity
import com.example.seabattle.domain.entity.Game
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



    // Function to get a game by gameId
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



    // Function to update the game data validating the game state.
    override suspend fun updateGameFields(gameId: String, logicFunction: (Game) -> Map<String, Any>): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val document = gamesCollection.document(gameId)

            db.runTransaction { transaction ->
                val snapshot = transaction.get(document)
                val fetchedGameDto = snapshot.toObject(GameDto::class.java)
                    ?: throw Exception("Game not found or invalid.")

                val gameEntity = fetchedGameDto.toGameEntity()

                var updateData = logicFunction(gameEntity)
                updateData = updateData + mapOf("updatedAt" to FieldValue.serverTimestamp())

                transaction.update(document, updateData)

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