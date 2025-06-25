package com.example.seabattle.data.realtimedb

import com.example.seabattle.domain.errors.PresenceError
import com.example.seabattle.domain.repository.PresenceRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    override suspend fun definePresence(userId: String) : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userStatusRef = realtimeDB.getReference("presence/${userId}/status")
            // Set user status to online and update last online timestamp
            userStatusRef.setValue("online").await()

            // Set up onDisconnect handlers to update status when user disconnects
            // This will be saved in the Realtime Database as 'task' to do when the user disconnects
            userStatusRef.onDisconnect().setValue("offline")
            return@runCatching
        }
        .recoverCatching { throwable ->
            throw throwable.toPresenceError()
        }
    }



    // Listen user presence status
    // It is necessary to listen to the presence of the user
    // If there is no listener, the app will be disconnected from the Realtime Database
    override fun listenUserPresence(userId: String): Flow<Result<String>> = callbackFlow {
        val userStatusRef = realtimeDB.getReference("presence/${userId}/status")

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val statusString = dataSnapshot.getValue(String::class.java)
                if (statusString == "online" || statusString == "offline") {
                    trySend(Result.success(statusString))
                } else {
                    trySend(Result.failure(
                        PresenceError.InvalidStatusValue(IllegalStateException("Unexpected presence status value: $statusString")))
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toPresenceError()))
            }
        }
        userStatusRef.addValueEventListener(listener)

        awaitClose {
            Timber.d("Closing listener for presence of user: $userId")
            userStatusRef.removeEventListener(listener)
        }
    }.flowOn(ioDispatcher)
}