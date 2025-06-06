package com.example.seabattle.data.firestore.repository

import com.example.seabattle.data.firestore.dto.RoomCreationDto
import com.example.seabattle.data.firestore.dto.RoomDto
import com.example.seabattle.data.firestore.errors.toRoomError
import com.example.seabattle.data.firestore.mappers.toRoomEntity
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.errors.RoomError
import com.example.seabattle.domain.repository.RoomRepository
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
import kotlin.collections.plus

class RoomRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : RoomRepository {
    private val roomsCollection = db.collection("rooms")
    private val listenerOptions = SnapshotListenOptions.Builder()
        .setMetadataChanges(MetadataChanges.INCLUDE)
        .build()
    // This variable is used to determine if the user is offline or online
    private var sourceIsServer: Boolean = true


    // Function to fetch all rooms with only one player
    override fun fetchRooms(userId: String) : Flow<Result<List<Room>>> = callbackFlow {
        val listener = roomsCollection
            .whereEqualTo("numberOfPlayers", 1)
            .whereEqualTo("roomState", RoomState.WAITING_FOR_PLAYER.name)
            .limit(25)
            .addSnapshotListener(listenerOptions) { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error.toRoomError()))
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

                val rooms = snapshot.documents
                    .mapNotNull { document ->
                        try {
                            document.toObject(RoomDto::class.java)?.toRoomEntity() ?: throw RoomError.RoomNotValid()
                        } catch (e: Exception) {
                            trySend(Result.failure(e.toRoomError()))
                            return@addSnapshotListener
                        }
                    }
                    // Exclude rooms created by the current user
                    // Didn't filtered in the query to avoid build a firestore index
                    .filter { room -> room.player1.userId != userId }
                trySend(Result.success(rooms))
            }
        awaitClose { listener.remove() }
    }.flowOn(ioDispatcher)



    // Function to listen for updates on a specific room
    override fun listenRoomUpdates(roomId: String) : Flow<Result<Room?>>
    = callbackFlow {
        val listener = roomsCollection
            .document(roomId)
            .addSnapshotListener(listenerOptions) { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error.toRoomError()))
                    return@addSnapshotListener
                }
                // If the snapshot is null, it means the room does not exist or has been deleted
                if (snapshot == null || !snapshot.exists()) {
                    trySend(Result.success(null))
                    return@addSnapshotListener
                }
                // If the snapshot is from cache, we assume the user is offline
                // and we don't update the room entity
                if (snapshot.metadata.isFromCache) {
                    sourceIsServer = false
                } else {
                    sourceIsServer = true
                    val roomEntity = try {
                        snapshot.toObject(RoomDto::class.java)?.toRoomEntity() ?: throw RoomError.RoomNotValid()
                    } catch (e: Exception) {
                        trySend(Result.failure(e.toRoomError()))
                        return@addSnapshotListener
                    }
                    trySend(Result.success(roomEntity))
                }
            }
        awaitClose { listener.remove() }
    }.flowOn(ioDispatcher)



    // Function to create a new room
    override suspend fun createRoom(room: Room) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            if (!sourceIsServer) {
                throw RoomError.NetworkConnection()
            }

            val roomDto = RoomCreationDto(
                roomId = room.roomId,
                roomName = room.roomName,
                roomState = room.roomState,
                player1 = room.player1,
            )
            roomsCollection.document(roomDto.roomId).set(roomDto).await()
            return@runCatching
        }
        .recoverCatching { throwable ->
            throw throwable.toRoomError()
        }
    }



    // Function to get the room details by roomId
    override suspend fun getRoom(roomId: String) : Result<Room>
    = withContext(ioDispatcher) {
        runCatching {
            val document = roomsCollection.document(roomId).get().await()
            if (!document.exists()) { throw RoomError.RoomNotFound() }

            val roomEntity = document.toObject(RoomDto::class.java)?.toRoomEntity() ?:
                throw RoomError.RoomNotValid()

            return@runCatching roomEntity
        }
        .recoverCatching { throwable ->
            throw throwable.toRoomError()
        }
    }



    // Function to update the room data validating the game state.
    override suspend fun updateRoomFields(roomId: String, logicFunction: (Room) -> Map<String, Any>): Result<Unit>
            = withContext(ioDispatcher) {
        runCatching {
            if (!sourceIsServer) {
                throw RoomError.NetworkConnection()
            }
            val document = roomsCollection.document(roomId)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(document)
                val fetchedGameDto = snapshot.toObject(RoomDto::class.java) ?: throw RoomError.RoomNotValid()

                val roomEntity = fetchedGameDto.toRoomEntity()

                var updateData = logicFunction(roomEntity)
                updateData = updateData + mapOf("updatedAt" to FieldValue.serverTimestamp())

                transaction.update(document, updateData)

                return@runTransaction
            }.await()
        }
        .recoverCatching { throwable ->
            throw throwable.toRoomError()
        }
    }



    // Function to delete a room by roomId
    override suspend fun deleteRoom(roomId: String) : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction { transaction ->
                val document = transaction.get(roomsCollection.document(roomId))

                if (document.exists()) {
                    transaction.delete(document.reference)
                }
                return@runTransaction
            }.await()
        }
        .recoverCatching { throwable ->
            throw throwable.toRoomError()
        }
    }
}