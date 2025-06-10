package com.example.seabattle.data.firestore.repository


import com.example.seabattle.data.firestore.dto.GameDto
import com.example.seabattle.data.firestore.errors.toGameError
import com.example.seabattle.data.firestore.mappers.toGameEntity
import com.example.seabattle.data.firestore.mappers.toGameCreationDto
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.errors.GameError
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


class GameRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : GameRepository {
    private val gamesCollection = db.collection("games")
    val listenerOptions = SnapshotListenOptions.Builder()
        .setMetadataChanges(MetadataChanges.INCLUDE)
        .build()
    // This variable is used to determine if the user is offline or online
    private var sourceIsServer: Boolean = true


    // Function to fetch all games with only one player
    override fun fetchGames(userId: String) : Flow<Result<List<Game>>> = callbackFlow {
        val listener = gamesCollection
            .whereEqualTo("player1.status", "online")
            .whereEqualTo("gameState", GameState.WAITING_FOR_PLAYER.name)
            .limit(25)
            .addSnapshotListener(listenerOptions) { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error.toGameError()))
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    trySend(Result.success(emptyList()))
                    return@addSnapshotListener
                }

                if (snapshot.metadata.isFromCache) {
                    sourceIsServer = false
                } else {
                    sourceIsServer = true
                }

                val games = snapshot.documents
                    .mapNotNull { document ->
                        try {
                            document.toObject(GameDto::class.java)?.toGameEntity() ?: throw GameError.GameNotValid()
                        } catch (e: Exception) {
                            trySend(Result.failure(e.toGameError()))
                            return@addSnapshotListener
                        }
                    }
                    // Exclude games created by the current user
                    // Didn't filtered in the query to avoid build a firestore index
                    .filter { game -> game.player1.userId != userId }
                trySend(Result.success(games))
            }
        awaitClose {
            Timber.d("Closing listener for fetchGames")
            listener.remove()
        }
    }.flowOn(ioDispatcher)



    // Function to listen for game updates
    override fun listenGameUpdates(gameId: String) : Flow<Result<Game>>
    = callbackFlow {

        val listener = gamesCollection
            .document(gameId)
            .addSnapshotListener(listenerOptions) { snapshot, error ->
                if (error != null) {
                    Timber.e(error, "Error listening to game updates for gameId: $gameId")
                    trySend(Result.failure(error.toGameError()))
                    return@addSnapshotListener
                }
                if (snapshot == null || !snapshot.exists()) {
                    trySend(Result.failure(GameError.GameNotFound()))
                    return@addSnapshotListener
                }
                if (!snapshot.metadata.isFromCache) {
                    val gameEntity = try {
                        snapshot.toObject(GameDto::class.java)?.toGameEntity() ?: throw GameError.GameNotValid()
                    } catch (e: Exception) {
                        trySend(Result.failure(e.toGameError()))
                        return@addSnapshotListener
                    }
                    trySend(Result.success(gameEntity))
                }
            }
        awaitClose {
            Timber.d("Closing listener for updates on gameId: $gameId")
            listener.remove()
        }
    }.flowOn(ioDispatcher)


    // Function to create a new game
    override suspend fun createGame(game: Game) : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            if (!sourceIsServer) {
                throw GameError.NetworkConnection()
            }
            val gameDto = game.toGameCreationDto()

            gamesCollection.document(game.gameId).set(gameDto).await()
            return@runCatching
        }
        .recoverCatching { throwable ->
            throw throwable.toGameError()
        }
    }



    // Function to get a game by gameId
    override suspend fun getGame(gameId: String): Result<Game> = withContext(ioDispatcher) {
        runCatching {
            val document = gamesCollection.document(gameId).get().await()
            if (!document.exists()) { throw GameError.GameNotFound() }

            val gameEntity = document.toObject(GameDto::class.java)?.toGameEntity() ?:
                throw GameError.GameNotValid()

            return@runCatching gameEntity
        }
        .recoverCatching { throwable ->
            throw throwable.toGameError()
        }
    }



    // Function to update the game data validating the game state.
    override suspend fun updateGameFields(gameId: String, logicFunction: (Game) -> Map<String, Any?>): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val document = gamesCollection.document(gameId)

            db.runTransaction { transaction ->
                val snapshot = transaction.get(document)
                val fetchedGameDto = snapshot.toObject(GameDto::class.java)
                    ?: throw GameError.GameNotValid()

                val gameEntity = fetchedGameDto.toGameEntity()

                var updateData = logicFunction(gameEntity)
                updateData = updateData + mapOf("updatedAt" to FieldValue.serverTimestamp())

                transaction.update(document, updateData)

                return@runTransaction
            }.await()
        }
        .recoverCatching { throwable ->
            throw throwable.toGameError()
        }
    }



    // Function to delete a game by gameId
    override suspend fun deleteGame(gameId: String) : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction { transaction ->
                val document = transaction.get(gamesCollection.document(gameId))

                if (document.exists()) {
                    transaction.delete(document.reference)
                }
                return@runTransaction
            }.await()
        }
        .recoverCatching { throwable ->
            throw throwable.toGameError()
        }
    }
}