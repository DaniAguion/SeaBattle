package com.example.seabattle.data.firestore.repository

import timber.log.Timber
import com.example.seabattle.data.firestore.dto.GameCreationDto
import com.example.seabattle.data.firestore.dto.RoomCreationDto
import com.example.seabattle.data.firestore.dto.RoomDto
import com.example.seabattle.data.firestore.mappers.toRoomEntity
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.entity.User
import com.example.seabattle.domain.entity.toBasic
import com.example.seabattle.domain.repository.PreGameRepository
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
import kotlin.collections.plus

class PreGameRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : PreGameRepository {

    private val roomsCollection = db.collection("rooms")
    private val gamesCollection = db.collection("games")


    // Function to fetch all rooms with only one player
    override fun fetchRooms() : Flow<Result<List<Room>>> = callbackFlow {
        val listener = roomsCollection
            .whereEqualTo("numberOfPlayers", 1)
            .whereEqualTo("roomState", RoomState.WAITING_FOR_PLAYER.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    trySend(Result.success(emptyList()))
                    return@addSnapshotListener
                }

                val rooms = snapshot.documents.mapNotNull { document ->
                    try {
                        document.toObject(RoomDto::class.java)?.toRoomEntity()
                    } catch (e: Exception) {
                        Timber.e(e, "Error to deserialize document: ${document.id}")
                        trySend(Result.failure(e))
                        return@addSnapshotListener
                    }
                }
                trySend(Result.success(rooms))
            }
        awaitClose { listener.remove() }
    }.flowOn(ioDispatcher)



    // Function to listen for updates on a specific room
    override fun listenRoomUpdates(roomId: String) : Flow<Result<Room>>
    = callbackFlow {
        val options = SnapshotListenOptions.Builder()
            .setMetadataChanges(MetadataChanges.INCLUDE)
            .build()

        val listener = roomsCollection
            .document(roomId)
            .addSnapshotListener(options) { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot == null || !snapshot.exists()) {
                    trySend(Result.failure(Exception("Room not found")))
                    return@addSnapshotListener
                }
                if (!snapshot.metadata.isFromCache()) {
                    val roomEntity = try {
                        snapshot.toObject(RoomDto::class.java)?.toRoomEntity()
                    } catch (e: Exception) {
                        Timber.e(e, "Error to deserialize roomId: $roomId")
                        trySend(Result.failure(e))
                        return@addSnapshotListener
                    }

                    if (roomEntity == null) {
                        trySend(Result.failure(Exception("Room not found")))
                        return@addSnapshotListener
                    }
                    trySend(Result.success(roomEntity))
                }
            }
        awaitClose { listener.remove() }
    }.flowOn(ioDispatcher)



    // Function to create a new room
    override suspend fun createRoom(roomId: String, roomName: String, user: User) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val roomDto = RoomCreationDto(
                roomId = roomId,
                roomName = roomName,
                roomState = RoomState.WAITING_FOR_PLAYER.name,
                player1 = user.toBasic(),
            )
            roomsCollection.document(roomDto.roomId).set(roomDto).await()
            return@runCatching
        }
    }



    // Function to use by the second player to join an existing room
    override suspend fun joinRoom(roomId: String, user: User) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction { transaction ->
                val document = roomsCollection.document(roomId)
                val snapshot = transaction.get(document)

                if (!snapshot.exists()) {
                    throw Exception("Room not found")
                }

                val roomDto = snapshot.toObject(RoomDto::class.java)
                    ?: throw Exception("Room data is corrupted")

                // Check if the room conditions are met for the second player to join
                if (roomDto.player1.userId == user.userId || roomDto.numberOfPlayers == 2 ||
                    roomDto.roomState != RoomState.WAITING_FOR_PLAYER.name) {
                    throw Exception("Room not available")
                }

                // Update the room state and add the second player
                val newData = mapOf(
                    "roomState" to RoomState.SECOND_PLAYER_JOINED.name,
                    "numberOfPlayers" to 2,
                    "player2" to user.toBasic(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )

                transaction.update(document, newData)
                return@runTransaction
            }.await()
        }
    }



    // Function to get the room details by roomId
    override suspend fun getRoom(roomId: String) : Result<Room>
    = withContext(ioDispatcher) {
        runCatching {
            val document = roomsCollection.document(roomId).get().await()
            if (document.exists()) {
                val roomEntity = document.toObject(RoomDto::class.java)?.toRoomEntity()
                if (roomEntity == null) {
                    throw Exception("Room not found")
                }
                return@runCatching roomEntity
            } else {
                throw Exception("Document not found")
            }
        }
    }



    // Function to delete a room by roomId
    override suspend fun deleteRoom(roomId: String) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction { transaction ->
                val document = roomsCollection.document(roomId)
                val snapshot = transaction.get(document)

                if (!snapshot.exists()) {
                    throw Exception("Room not found")
                }

                val roomDto = snapshot.toObject(RoomDto::class.java)
                    ?: throw Exception("Room data is corrupted")

                // Room can be deleted only if it is in WAITING_FOR_PLAYER or GAME_STARTING state
                if (roomDto.roomState == RoomState.WAITING_FOR_PLAYER.name ||
                    roomDto.roomState == RoomState.GAME_STARTING.name)
                {
                    transaction.delete(document)
                } else {
                    throw Exception("Room cannot be deleted in current state: ${roomDto.roomState}")
                }
                return@runTransaction
            }.await()
        }
    }



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


    // Function to update the room data validating the game state.
    override suspend fun updateGameFields(roomId: String, logicFunction: (Room) -> Map<String, Any>): Result<Unit>
            = withContext(ioDispatcher) {
        runCatching {
            val document = roomsCollection.document(roomId)

            db.runTransaction { transaction ->
                val snapshot = transaction.get(document)
                val fetchedGameDto = snapshot.toObject(RoomDto::class.java)
                    ?: throw Exception("Room not found or invalid.")

                val roomEntity = fetchedGameDto.toRoomEntity()

                var updateData = logicFunction(roomEntity)
                updateData = updateData + mapOf("updatedAt" to FieldValue.serverTimestamp())

                transaction.update(document, updateData)

                return@runTransaction
            }.await()
        }
    }
}