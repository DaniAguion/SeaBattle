package com.example.seabattle.data.firestore.repository

import com.example.seabattle.data.firestore.dto.UserDto
import com.example.seabattle.data.firestore.mappers.toUserDto
import com.example.seabattle.data.firestore.mappers.toUserEntity
import com.example.seabattle.domain.repository.UserRepository
import com.example.seabattle.data.firestore.errors.toUserError
import com.example.seabattle.domain.entity.User
import com.example.seabattle.domain.errors.UserError
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    private val usersCollection = db.collection("users")


    override suspend fun createUser(user: User) : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userDto = user.toUserDto()
            usersCollection.document(user.userId).set(userDto).await()
        }
        .map { _ -> }
        .recoverCatching { throwable ->
            throw throwable.toUserError()
        }
    }


    override suspend fun getUser(userId: String) : Result<User> = withContext(ioDispatcher) {
        runCatching {
            val document = usersCollection.document(userId).get().await()
            if (document.exists()) {
                val userEntity = document.toObject(UserDto::class.java)?.toUserEntity()
                return@runCatching userEntity ?: throw UserError.UserProfileNotFound()
            } else {
                throw UserError.UserProfileNotFound()
            }
        }
        .recoverCatching { throwable ->
            throw throwable.toUserError()
        }
    }


    override suspend fun getLeaderboard(): Result<List<User>> = withContext(ioDispatcher) {
        runCatching {
            val querySnapshot = usersCollection.orderBy("score", DESCENDING).limit(25).get().await()
            val usersList = querySnapshot.documents.mapNotNull { it.toObject(UserDto::class.java)?.toUserEntity() }
            return@runCatching usersList
        }
        .recoverCatching { throwable ->
            throw throwable.toUserError()
        }
    }


    override suspend fun getUserPosition(userId: String, userScore: Int): Result<Int> = withContext(ioDispatcher) {
        runCatching {
            val querySnapshot = usersCollection.whereGreaterThanOrEqualTo("score", userScore).orderBy("score", DESCENDING).get().await()
            val usersList = querySnapshot.documents.mapNotNull { it.toObject(UserDto::class.java)?.toUserEntity() }
            return@runCatching usersList.indexOfFirst { it.userId == userId } + 1
        }
        .recoverCatching { throwable ->
            throw throwable.toUserError()
        }
    }
}