package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.User
import com.example.seabattle.domain.errors.DataError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameBoardRepository
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.UserGamesRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID


class CreateGameUseCase(
    val gameRepository: GameRepository,
    val userRepository: UserRepository,
    val gameBoardRepository: GameBoardRepository,
    val userGamesRepository: UserGamesRepository,
    val ioDispatcher: CoroutineDispatcher,
    val sessionService: SessionService,
) {
    suspend operator fun invoke(gameName: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()
            val user = userRepository.getUser(userId).getOrThrow()

            // Create the game in the repository
            val gameId = UUID.randomUUID().toString()
            gameBoardRepository.createGameBoard().getOrThrow()
            val boardForPlayer1 = gameBoardRepository.getGameBoard()
            val player2Ships = gameBoardRepository.getShipList()
            gameBoardRepository.createGameBoard().getOrThrow()
            val boardForPlayer2 = gameBoardRepository.getGameBoard()
            val player1Ships = gameBoardRepository.getShipList()

            var game = Game(
                gameId = gameId,
                gameName = gameName,
                player1 = user,
                boardForPlayer1 = boardForPlayer1,
                player1Ships = player1Ships,
                player2 = User(
                    userId = "",
                    displayName = "",
                    photoUrl = ""
                ),
                boardForPlayer2 = boardForPlayer2,
                player2Ships = player2Ships,
                gameState = GameState.WAITING_FOR_PLAYER.name
            )

            // Create the game in the repository
            gameRepository.createGame(game).getOrThrow()
            // Add the game to the user's games
            userGamesRepository.updateCurrentGameId(userId, gameId).getOrThrow()
            // Register the gameId in the session
            sessionService.setCurrentGameId(gameId)
            return@runCatching
        }
            .onFailure { e ->
                Timber.e(e, "CreateGameUseCase failed.")
            }
            .recoverCatching { throwable ->
                when (throwable) {
                    is GameError -> throw throwable
                    is UserError -> throw throwable
                    is DataError -> throw throwable
                    else -> throw DomainError.Unknown(throwable)
                }
            }
    }
}