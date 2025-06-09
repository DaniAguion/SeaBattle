package com.example.seabattle.data.realtimedb

import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.repository.PresenceRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class PresenceRepoImpl(
    private val realtimeDB: FirebaseDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : PresenceRepository {


    // Set user presence status to online
    override suspend fun definePresence(userId: String) = withContext(ioDispatcher) {
        val userStatusRef = realtimeDB.getReference("presence/${userId}")
        val lastOnlineRef = realtimeDB.getReference("presence/${userId}/lastOnline")

        // Set user status to online and update last online timestamp
        userStatusRef.setValue("online").await()
        lastOnlineRef.setValue(ServerValue.TIMESTAMP).await()

        // Set up onDisconnect handlers to update status when user disconnects
        // This will be saved in the Realtime Database as 'task' to do when the user disconnects
        userStatusRef.onDisconnect().setValue("offline")
        lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP)
    }



    // Listen user presence status
    override fun listenUserPresence(userId: String): Flow<Result<Boolean>> = callbackFlow {
        val presenceRef = realtimeDB.getReference("presence/${userId}/status")

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val statusString = dataSnapshot.getValue(String::class.java)
                if (statusString != null) {
                    when (statusString) {
                        "online" -> trySend(Result.success(true))
                        "offline" -> trySend(Result.success(false))
                        else -> {
                            trySend(Result.failure(DomainError.PresenceError()))
                        }
                    }
                } else {
                    trySend(Result.failure(DomainError.PresenceError()))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(DomainError.PresenceError()))
            }
        }
        presenceRef.addValueEventListener(listener)

        awaitClose {
            Timber.d("Closing listener for presence of user: $userId")
            presenceRef.removeEventListener(listener)
        }
    }.flowOn(ioDispatcher)
}