package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.UserBasic
import com.example.seabattle.domain.entity.toBasic
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameBoardRepository
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID


class CreateGameUseCase(
    val gameRepository: GameRepository,
    val userRepository: UserRepository,
    val gameBoardRepository: GameBoardRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    suspend operator fun invoke(gameName: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = session.getCurrentUserId()
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
                player1 = user.toBasic(),
                boardForPlayer1 = boardForPlayer1,
                player1Ships = player1Ships,
                player2 = UserBasic(
                    userId = "",
                    displayName = "",
                    photoUrl = ""
                ),
                boardForPlayer2 = boardForPlayer2,
                player2Ships = player2Ships,
                gameState = GameState.WAITING_FOR_PLAYER.name
            )

            gameRepository.createGame(game).getOrThrow()

            // Fetch the updated game and set it in the session
            game = gameRepository.getGame(gameId).getOrThrow()
            session.setCurrentGame(game)
            return@runCatching
        }
            .onFailure { e ->
                Timber.e(e, "CreateRoomUseCase failed.")
            }
            .recoverCatching { throwable ->
                if (throwable is GameError) throw throwable
                else if (throwable is UserError) throw throwable
                else throw DomainError.Unknown(throwable)
            }
    }
}