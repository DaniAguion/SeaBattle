package com.example.seabattle.domain.usecase.userGames

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.BasicPlayer
import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.domain.errors.DataError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.UserGamesRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class InviteUserUseCase(
    val userRepository: UserRepository,
    val userGamesRepository: UserGamesRepository,
    val ioDispatcher: CoroutineDispatcher,
    val sessionService: SessionService,
) {
    suspend operator fun invoke(invitedPlayerId: String, gameId: String): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()

            if (invitedPlayerId.isEmpty() || userId == invitedPlayerId) {
                throw UserError.InvalidGuest()
            }

            val user = userRepository.getUserById(userId).getOrThrow()
            val invitedPlayer = userRepository.getUserById(invitedPlayerId).getOrThrow()

            val invitation = Invitation(
                gameId = gameId,
                invitedTo = BasicPlayer(
                    userId = invitedPlayer.userId,
                    displayName = invitedPlayer.displayName,
                    photoUrl = invitedPlayer.photoUrl
                ),
                invitedBy = BasicPlayer(
                    userId = user.userId,
                    displayName = user.displayName,
                    photoUrl = user.photoUrl
                )
            )

            userGamesRepository.sendInvitation(invitation).getOrThrow()
        }
        .onFailure { e ->
            Timber.e(e, "InviteUserUseCase failed.")
        }
        .recoverCatching { throwable ->
            when (throwable) {
                is UserError -> throw throwable
                is DataError -> throw throwable
                else -> throw DomainError.Unknown(throwable)
            }
        }
    }
}