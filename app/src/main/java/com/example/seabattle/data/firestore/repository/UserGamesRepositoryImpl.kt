package com.example.seabattle.data.firestore.repository

import com.example.seabattle.data.firestore.dto.UserGamesDto
import com.example.seabattle.data.firestore.errors.toDataError
import com.example.seabattle.data.firestore.mappers.toDto
import com.example.seabattle.data.firestore.mappers.toEntity
import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.domain.entity.UserGames
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.UserGamesRepository
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
import timber.log.Timber

class UserGamesRepositoryImpl(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : UserGamesRepository {
    private val userGamesCollection = db.collection("userGames")

    val listenerOptions = SnapshotListenOptions.Builder()
        .setMetadataChanges(MetadataChanges.INCLUDE)
        .build()


    // Function to create user games
    override suspend fun createUserGames(userId: String) : Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val userGamesDto = UserGamesDto(userId = userId)
            userGamesCollection
                .document(userId)
                .set(userGamesDto)
                .await()
        }
        .map { _ -> }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    // Function to get user games by user ID
    override suspend fun getUserGames(userId: String) : Result<UserGames>
    = withContext(ioDispatcher) {
        runCatching {
            val document = userGamesCollection.document(userId).get().await()
            if (document.exists()) {
                val userGamesEntity = document.toObject(UserGamesDto::class.java)?.toEntity()
                return@runCatching userGamesEntity ?: throw UserError.InvalidData()
            } else {
                throw UserError.UserGamesNotFound()
            }
        }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    // Function to delete user games
    override suspend fun deleteUserGames(userId: String): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            userGamesCollection
                .document(userId)
                .delete()
                .await()
        }
        .map { _ -> }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    // Function to update the current game ID for a user
    override suspend fun updateCurrentGameId(userId: String, gameId: String?): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            userGamesCollection
                .document(userId)
                .update("currentGameId", gameId)
                .await()
        }
        .map { _ -> }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }


    // Function to invite an user
    override suspend fun inviteToGame(guestId: String, invitation: Invitation): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val invitationDto = invitation.toDto()
            userGamesCollection
                .document(guestId)
                .update("invitedGameId", FieldValue.arrayUnion(invitationDto))
                .await()
        }
            .map { _ -> }
            .recoverCatching { throwable ->
                throw throwable as? UserError ?: throwable.toDataError()
            }
    }



    // Function to listen to invitations for the user
    override fun listenToUserGames(userId: String): Flow<Result<UserGames>>
    = callbackFlow {
        val listener = userGamesCollection
            .document(userId)
            .addSnapshotListener(listenerOptions) { snapshot, error ->
                if (error != null) {
                    Timber.e(error, "Error listening to user games updates")
                    trySend(Result.failure(error.toDataError()))
                    return@addSnapshotListener
                }
                if (snapshot == null || !snapshot.exists()) {
                    trySend(Result.failure(UserError.UserGamesNotFound()))
                    return@addSnapshotListener
                }
                if (!snapshot.metadata.isFromCache) {
                    val userGamesEntity = try {
                        snapshot.toObject(UserGamesDto::class.java)?.toEntity()
                            ?: throw UserError.InvalidData()
                    } catch (e: Exception) {
                        trySend(Result.failure(e.toDataError()))
                        return@addSnapshotListener
                    }
                    trySend(Result.success(userGamesEntity))
                }
            }
        awaitClose {
            Timber.d("Closing listener for user games updates")
            listener.remove()
        }
    }.flowOn(ioDispatcher)



    // Function to delete an invitation
    override suspend fun deleteInvitation(userId: String, gameID: String): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val document = userGamesCollection
                .document(userId)
                .get()
                .await()

            var userGamesDto: UserGamesDto?

            if (document.exists()) {
                userGamesDto = document.toObject(UserGamesDto::class.java)
                    ?: throw UserError.InvalidData()
            } else {
                throw UserError.UserGamesNotFound()
            }

            val gameInvitation = userGamesDto.gameInvitations.indexOfFirst { it.gameId == gameID }

            userGamesCollection
                .document(userId)
                .update("invitedGameId", FieldValue.arrayRemove(gameInvitation))
                .await()

            return@runCatching
        }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }
}