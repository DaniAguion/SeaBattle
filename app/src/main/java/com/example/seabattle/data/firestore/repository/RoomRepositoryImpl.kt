package com.example.seabattle.data.firestore.repository

import android.util.Log
import com.example.seabattle.data.firestore.mappers.toCreationDTO
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.repository.RoomRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RoomRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : RoomRepository {

    private val roomsCollection = db.collection("rooms")
    private val tag = "RoomRepository"

    override suspend fun createRoom(room: Room): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val roomDTO = room.toCreationDTO()
            roomsCollection.document(roomDTO.roomId)
                .set(roomDTO)
                .await()
        }
        .map { _ -> }
        .onFailure { e ->
            Log.e(tag, "Error creating room with Id: ${room.roomId}. ${e.message}")
        }
    }



    override suspend fun fetchRooms():  Result<List<Room>>
    = withContext(ioDispatcher) {
        runCatching {
            val rooms = mutableListOf<Room>()
            val snapshot =
                roomsCollection.whereEqualTo("numberOfPlayers", 1).get(Source.SERVER).await()
            for (document in snapshot.documents) {
                val room = document.toObject(Room::class.java)
                if (room != null) {
                    rooms.add(room)
                }
            }
            rooms.toList()
        }
        .onFailure { e ->
            Log.e("FirestoreRepository", "Error fetching rooms: ${e.message}")
            emptyList<Room>()
        }
    }


    override suspend fun getRoom(roomId: String): Result<Room?>
    = withContext(ioDispatcher) {
        runCatching {
            val document = roomsCollection.document(roomId).get().await()
            if (document.exists()) {
                val roomEntity = document.toObject(Room::class.java)
                roomEntity
            } else {
                throw Exception("User not found")
            }
        }
        .onFailure { e ->
            Log.e("FirestoreRepository", "Error fetching rooms: ${e.message}")
            emptyList<Room>()
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


    override suspend fun deleteRoom(roomId: String): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            roomsCollection.document(roomId).delete().await()
        }
        .map { _ -> }
        .onFailure { e ->
            Log.e(tag, "Error deleting room with Id: ${roomId}. ${e.message}")
        }
    }
}