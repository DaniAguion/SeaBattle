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
import kotlin.collections.plus

class RoomRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : RoomRepository {
    private val roomsCollection = db.collection("rooms")


    // Function to fetch all rooms with only one player
    override fun fetchRooms() : Flow<Result<List<Room>>> = callbackFlow {
        val listener = roomsCollection
            .whereEqualTo("numberOfPlayers", 1)
            .whereEqualTo("roomState", RoomState.WAITING_FOR_PLAYER.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error.toRoomError()))
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    trySend(Result.success(emptyList()))
                    return@addSnapshotListener
                }

                val rooms = snapshot.documents.mapNotNull { document ->
                    try {
                        document.toObject(RoomDto::class.java)?.toRoomEntity() ?: throw RoomError.RoomNotValid()
                    } catch (e: Exception) {
                        trySend(Result.failure(e.toRoomError()))
                        return@addSnapshotListener
                    }
                }
                trySend(Result.success(rooms))
            }
        awaitClose { listener.remove() }
    }.flowOn(ioDispatcher)



    // Function to listen for updates on a specific room
    override fun listenRoomUpdates(roomId: String) : Flow<Result<Room?>>
    = callbackFlow {
        val options = SnapshotListenOptions.Builder()
            .setMetadataChanges(MetadataChanges.INCLUDE)
            .build()

        val listener = roomsCollection
            .document(roomId)
            .addSnapshotListener(options) { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error.toRoomError()))
                    return@addSnapshotListener
                }
                if (snapshot == null || !snapshot.exists()) {
                    trySend(Result.success(null))
                    return@addSnapshotListener
                }
                if (!snapshot.metadata.isFromCache) {
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