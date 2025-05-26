package com.example.seabattle.data.firestore.repository

import android.util.Log
import com.example.seabattle.data.firestore.dto.RoomDtoRd
import com.example.seabattle.data.firestore.mappers.toRoomDto
import com.example.seabattle.data.firestore.mappers.toRoomEntity
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.entity.User
import com.example.seabattle.domain.entity.toBasic
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

class RoomRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : RoomRepository {

    private val roomsCollection = db.collection("rooms")
    private val tag = "RoomRepository"


    // Function to fetch all rooms with only one player
    override fun fetchRooms() : Flow<Result<List<Room>>> = callbackFlow {
        val listener = roomsCollection
            .whereEqualTo("numberOfPlayers", 1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }

                val rooms = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(RoomDtoRd::class.java)?.toRoomEntity()
                } ?: emptyList()

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
                    val roomEntity = snapshot.toObject(RoomDtoRd::class.java)?.toRoomEntity()
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
    override suspend fun createRoom(room: Room) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val roomDto = room.toRoomDto()
            roomsCollection.document(roomDto.roomId).set(roomDto).await()
            return@runCatching Unit
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

                val roomDto = snapshot.toObject(RoomDtoRd::class.java)
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
                return@runTransaction Unit
            }.await()
        }
    }



    // Function to get the room details by roomId
    override suspend fun getRoom(roomId: String) : Result<Room>
    = withContext(ioDispatcher) {
        runCatching {
            val document = roomsCollection.document(roomId).get().await()
            if (document.exists()) {
                val roomEntity = document.toObject(RoomDtoRd::class.java)?.toRoomEntity()
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

                val roomDto = snapshot.toObject(RoomDtoRd::class.java)
                    ?: throw Exception("Room data is corrupted")

                // Check if the room conditions are met for deletion
                if (roomDto.roomState == RoomState.WAITING_FOR_PLAYER.name ||
                    roomDto.roomState == RoomState.GAME_STARTED.name)
                {
                    transaction.delete(document)
                } else {
                    throw Exception("Room cannot be deleted in current state: ${roomDto.roomState}")
                }
                return@runTransaction Unit
            }.await()
        }
    }


    override suspend fun updateRoom(roomId: String, newData: Map<String, Any>) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val document = roomsCollection.document(roomId).get().await()
            if (document.exists()) {
                val updatedRoom = newData + mapOf(
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                roomsCollection.document(roomId).update(updatedRoom).await()
            } else {
                throw Exception("Room not found")
            }
        }
            .map { _ -> }
            .onFailure { e ->
                Log.e(tag, "Error updating room: ${roomId}. ${e.message}")
            }
    }



    override suspend fun updateRoomState(roomId: String, userId: String) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction { transaction ->
                val document = roomsCollection.document(roomId)
                val snapshot = transaction.get(document)

                if (!snapshot.exists()) {
                    throw Exception("Room not found")
                }

                val roomDto = snapshot.toObject(RoomDtoRd::class.java)
                    ?: throw Exception("Room data is corrupted")

                var newData: Map<String, Any> = mapOf(
                    "updatedAt" to FieldValue.serverTimestamp()
                )

                when (userId) {
                    roomDto.player1.userId -> {
                        if (roomDto.roomState == RoomState.SECOND_PLAYER_JOINED.name) {
                            // TO DO
                        }
                    }
                    roomDto.player2?.userId -> {

                    }
                    else -> throw Exception("User does not belong to this game")
                }


                transaction.update(document, newData)
                return@runTransaction Unit
            }.await()
        }
    }
}