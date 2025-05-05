package com.example.seabattle.data.firestore.repository

import android.util.Log
import com.example.seabattle.domain.repository.UserRepository
import com.example.seabattle.domain.entity.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    private val usersCollection = db.collection("users")
    private val tag = "UserRepository"


    override suspend fun createUser(user: User) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            usersCollection.document(user.userId).set(user).await()
        }
        .map { _ -> }
        .onFailure { e ->
            Log.e(tag, "Error creating user profile: ${e.message}")
        }
    }


    override suspend fun getUser(userId: String) : Result<User?>
    = withContext(ioDispatcher) {
        runCatching {
            val document = usersCollection.document(userId).get().await()
            if (document.exists()) {
                val userEntity = document.toObject(User::class.java)
                userEntity
            } else {
                throw Exception("User not found")
            }
        }
        .onFailure { e ->
            Log.e(tag, "Error creating user profile: ${e.message}")
        }
    }
}