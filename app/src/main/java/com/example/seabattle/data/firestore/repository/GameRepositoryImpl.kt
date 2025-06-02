package com.example.seabattle.data.firestore.repository

import com.example.seabattle.data.firestore.dto.GameCreationDto
import com.example.seabattle.data.firestore.dto.GameDto
import com.example.seabattle.data.firestore.dto.RoomDto
import com.example.seabattle.data.firestore.mappers.toGameEntity
import com.example.seabattle.data.firestore.mappers.toRoomEntity
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Room
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
    private val roomsCollection = db.collection("rooms")
    private val gamesCollection = db.collection("games")


    // Function to listen for game updates
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
                if (!snapshot.metadata.isFromCache) {
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



    // Function to create a new game from the room
    override suspend fun createGame(roomId: String, logicFunction: (Room) -> Game, updatedRoomData: Map<String, Any>) : Result<String>
            = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction {  transaction ->
                val roomDocument = roomsCollection.document(roomId)
                val roomSnapshot = transaction.get(roomDocument)
                if (!roomSnapshot.exists()) {
                    throw Exception("Room not found")
                }
                val room = roomSnapshot.toObject(RoomDto::class.java)?.toRoomEntity()
                    ?: throw Exception("Room data is corrupted")

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
    }



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
            return@runCatching
        }
    }
}