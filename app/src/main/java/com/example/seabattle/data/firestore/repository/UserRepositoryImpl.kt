package com.example.seabattle.data.firestore.repository

import android.util.Log
import com.example.seabattle.data.firestore.dto.UserDto
import com.example.seabattle.data.firestore.mappers.toUserDto
import com.example.seabattle.data.firestore.mappers.toUserEntity
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
            val userDto = user.toUserDto()
            usersCollection.document(user.userId).set(userDto).await()
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
                val userEntity = document.toObject(UserDto::class.java)?.toUserEntity()
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