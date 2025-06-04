package com.example.seabattle.data.firestore.repository

import com.example.seabattle.data.firestore.dto.GameCreationDto
import com.example.seabattle.data.firestore.dto.GameDto
import com.example.seabattle.data.firestore.dto.RoomDto
import com.example.seabattle.data.firestore.errors.toGameError
import com.example.seabattle.data.firestore.errors.toRoomError
import com.example.seabattle.data.firestore.mappers.toGameEntity
import com.example.seabattle.data.firestore.mappers.toRoomEntity
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.RoomError
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
    private val roomsCollection = db.collection("rooms")
    private val gamesCollection = db.collection("games")
    val listenerOptions = SnapshotListenOptions.Builder()
        .setMetadataChanges(MetadataChanges.INCLUDE)
        .build()


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
                        trySend(Result.failure(e.toRoomError()))
                        return@addSnapshotListener
                    }
                    trySend(Result.success(gameEntity))
                }
            }
        awaitClose { listener.remove() }
    }.flowOn(ioDispatcher)



    // Function to create a new game from the room
    override suspend fun createGame(roomId: String, logicFunction: (Room) -> Game, updatedRoomData: Map<String, Any>) : Result<String>
            = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction {  transaction ->
                val roomDocument = roomsCollection.document(roomId)
                val roomSnapshot = transaction.get(roomDocument)
                if (!roomSnapshot.exists()) { throw RoomError.RoomNotFound() }

                val room = roomSnapshot.toObject(RoomDto::class.java)?.toRoomEntity()
                    ?: throw RoomError.RoomNotValid()

                val game = logicFunction(room)

                // Create the game document
                val gameCreationDto = GameCreationDto(
                    gameId = game.gameId,
                    player1 = game.player1,
                    boardForPlayer1 = game.boardForPlayer1,
                    player1Ships = game.player1Ships,
                    player2 = game.player2,
                    boardForPlayer2 = game.boardForPlayer2,
                    player2Ships = game.player2Ships,
                    gameState = game.gameState,
                    currentPlayer = game.currentPlayer,
                )
                transaction.set(gamesCollection.document(game.gameId), gameCreationDto)

                // Update the room document to indicate the game has been created
                val updateData = updatedRoomData + mapOf("updatedAt" to FieldValue.serverTimestamp())
                transaction.update(roomDocument, updateData)
                return@runTransaction game.gameId
            }.await()
        }
        .recoverCatching { throwable ->
            // The errors that can be thrown here are related to the room data
            throw throwable.toRoomError()
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