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



    // Function to delete user games
    override suspend fun deleteUserGames(userId: String): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            userGamesCollection
                .document(userId)
                .delete()
                .await()
            return@runCatching
        }
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
            return@runCatching
        }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }



    // Function to invite an user
    override suspend fun sendInvitation(invitation: Invitation): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction { transaction ->
                val invitationDto = invitation.toDto()
                val documentHostRef = userGamesCollection.document(invitationDto.invitedBy.userId)
                val documentGuestRef = userGamesCollection.document(invitationDto.invitedTo.userId)

                transaction.update(
                    documentHostRef,
                    "sentGameInvitation",
                    invitationDto
                )

                transaction.update(
                    documentGuestRef,
                    "invitedGameId",
                    FieldValue.arrayUnion(invitationDto)
                )
            }.await()
            return@runCatching
        }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }



    // Function to delete an invitation
    override suspend fun cancelInvitation(userId: String): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            db.runTransaction { transaction ->
                val userGamesDoc = userGamesCollection.document(userId)

                // Fetch the user games document to check the sent invitation
                val snapshot = transaction.get(userGamesDoc)
                if (!snapshot.exists()) throw UserError.UserGamesNotFound()
                val userGamesDto = snapshot.toObject(UserGamesDto::class.java) ?: throw UserError.InvalidData()
                val invitationDto = userGamesDto.sentGameInvitation ?: return@runTransaction

                // Remove the invitation from the host and guest user games
                val documentHostRef = userGamesCollection.document(invitationDto.invitedBy.userId)
                val documentGuestRef = userGamesCollection.document(invitationDto.invitedTo.userId)

                transaction.update(
                    documentHostRef,
                    "sentGameInvitation",
                    null
                )

                transaction.update(
                    documentGuestRef,
                    "invitedGameId",
                    FieldValue.arrayRemove(invitationDto)
                )
            }.await()
            return@runCatching
        }
        .recoverCatching { throwable ->
            throw throwable as? UserError ?: throwable.toDataError()
        }
    }
}