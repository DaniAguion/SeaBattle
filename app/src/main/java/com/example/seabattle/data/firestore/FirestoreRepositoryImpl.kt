package com.example.seabattle.data.firestore

import android.util.Log
import com.example.seabattle.data.firestore.entities.GameEntity
import com.example.seabattle.data.firestore.entities.RoomEntity
import com.example.seabattle.data.firestore.mappers.toDomainModel
import com.example.seabattle.data.firestore.mappers.toEntity
import com.example.seabattle.domain.firestore.FirestoreRepository
import com.example.seabattle.domain.model.Game
import com.example.seabattle.domain.model.Room
import com.example.seabattle.domain.model.User
import com.example.seabattle.domain.model.UserBasic
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRepositoryImpl(
    private val db: FirebaseFirestore
) : FirestoreRepository {

    private val usersCollection = db.collection("users")
    private val roomsCollection = db.collection("rooms")
    private val gamesCollection = db.collection("games")
    private val tag = "FirestoreRepository"

    //
    // Functions to manage users
    //

    override suspend fun createUser(user: User): Boolean {
        return try {
            usersCollection.document(user.userId).set(user).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error creating user profile: ${e.message}")
            false
        }
    }

    override suspend fun getUser(userId: String): User? {
        try {
            val document = usersCollection.document(userId).get().await()
            if (document.exists()) {
                val userEntity = document.toObject(User::class.java)
                if (userEntity != null) {
                    Log.d("FirestoreRepository", "User profile retrieved: ${userEntity.displayName}")
                    return userEntity
                }
            }
            return null
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting user profile: ${e.message}")
            return null
        }
    }


    //
    // Functions to manage rooms
    //

    override suspend fun getRoom(roomId: String): Room? {
        try {
            val document = roomsCollection.document(roomId).get().await()
            if (document.exists()) {
                val roomEntity = document.toObject(RoomEntity::class.java)
                if (roomEntity != null) {
                    return roomEntity.toDomainModel()
                }
            }
            return null
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting room: ${e.message}")
            return null
        }
    }


    override suspend fun fetchRooms(): List<Room> {
        return try {
            val snapshot = roomsCollection.get().await()
            val rooms = mutableListOf<Room>()
            for (document in snapshot.documents) {
                val roomEntity = document.toObject(RoomEntity::class.java)
                if (roomEntity != null) {
                    rooms.add(roomEntity.toDomainModel())
                }
            }
            rooms
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error fetching rooms: ${e.message}")
            emptyList()
        }
    }


    override suspend fun createRoom(room: Room): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val entity = room.toEntity()
            roomsCollection.document(entity.roomId)
                .set(entity)
                .await()
        }
        .map { _ -> }
        .onFailure { e ->
            Log.e(tag, "Error creating room with Id: ${room.roomId}. ${e.message}")
        }
    }



    override suspend fun updateRoom(room: Room): Boolean {
        return try {
            val document = roomsCollection.document(room.roomId).get().await()
            if (document.exists()) {
                // TO DO: update the room with the new data
                val newData = mapOf(
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                roomsCollection.document(room.roomId).update(newData).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error updating room: ${e.message}")
            false
        }
    }


    override suspend fun deleteRoom(roomId: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            roomsCollection.document(roomId).delete().await()
        }
        .map { _ -> }
        .onFailure { e ->
            Log.e(tag, "Error deleting room with Id: ${roomId}. ${e.message}")
        }
    }



    //
    // Functions to manage games
    //

    override suspend fun createGame(game: Game): Boolean {
        return try {
            val gameEntity = game.toEntity()
            gamesCollection.document(gameEntity.gameId).set(gameEntity).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error creating game: ${e.message}")
            false
        }
    }


    override suspend fun joinGame(gameId: String, player2: UserBasic, gameState: String): Boolean {
        return try {
            val document = gamesCollection.document(gameId).get().await()
            if (document.exists()) {
                val newData = mapOf(
                    "player2" to player2,
                    "gameState" to gameState
                )
                gamesCollection.document(gameId).update(newData).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error updating game: ${e.message}")
            true
        }
    }

    override suspend fun getGame(gameId: String): Game? {
        try {
            val document = gamesCollection.document(gameId).get().await()
            if (document.exists()) {
                val gameEntity = document.toObject(GameEntity::class.java)
                if (gameEntity != null) {
                    return gameEntity.toDomainModel()
                }
            }
            return null
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting game: ${e.message}")
            return null
        }
    }


    override suspend fun updateGame(game: Game): Boolean {
        return try {
            val document = gamesCollection.document(game.gameId).get().await()
            if (document.exists()) {
                val newData = mapOf(
                    "player1Board" to game.player1Board,
                    "player2Board" to game.player2Board,
                    "actualTurn" to game.currentTurn,
                    "currentPlayer" to game.currentPlayer,
                    "updatedAt" to FieldValue.serverTimestamp(),
                    "gameFinished" to game.gameFinished,
                    "winner" to (game.winnerId ?: ""),
                )
                db.collection("games").document(game.gameId).update(newData).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error updating game: ${e.message}")
            true
        }
    }
}