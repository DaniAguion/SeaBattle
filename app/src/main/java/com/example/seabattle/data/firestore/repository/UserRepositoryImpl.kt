package com.example.seabattle.data.firestore.repository

import com.example.seabattle.data.firestore.dto.BasicPlayerDto
import com.example.seabattle.data.firestore.dto.UserDto
import com.example.seabattle.data.firestore.errors.toDataError
import com.example.seabattle.data.firestore.mappers.toDto
import com.example.seabattle.data.firestore.mappers.toEntity
import com.example.seabattle.domain.entity.BasicPlayer
import com.example.seabattle.domain.repository.UserRepository
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
            val userDto = user.toDto()

            usersCollection
                .document(user.userId)
                .set(userDto)
                .await()
        }
        .map { _ -> }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    override suspend fun getUser(userId: String) : Result<User> = withContext(ioDispatcher) {
        runCatching {
            val document = usersCollection
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                val userEntity = document.toObject(UserDto::class.java)?.toEntity()
                return@runCatching userEntity ?: throw UserError.UserProfileNotFound()
            } else {
                throw UserError.UserProfileNotFound()
            }
        }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    override suspend fun deleteUser(userId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            usersCollection
                .document(userId)
                .delete()
                .await()
        }
        .map { _ -> }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    override suspend fun getLeaderboard(): Result<List<BasicPlayer>> = withContext(ioDispatcher) {
        runCatching {
            val querySnapshot = usersCollection
                .orderBy("score", DESCENDING)
                .orderBy("userId", DESCENDING)
                .limit(25)
                .get()
                .await()

            val usersList : List<BasicPlayer> = querySnapshot.documents.mapNotNull { it.toObject(BasicPlayerDto::class.java)?.toEntity() }
            return@runCatching usersList
        }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    override suspend fun getUserPosition(userId: String, userScore: Int): Result<Int> = withContext(ioDispatcher) {
        runCatching {
            val querySnapshot = usersCollection
                .whereGreaterThanOrEqualTo("score", userScore)
                .orderBy("score", DESCENDING)
                .get()
                .await()

            val usersList = querySnapshot.documents.mapNotNull { it.toObject(UserDto::class.java)?.toEntity() }
            return@runCatching usersList.indexOfFirst { it.userId == userId } + 1
        }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }
}