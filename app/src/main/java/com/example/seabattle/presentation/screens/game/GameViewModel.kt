package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.BasicCellState
import com.example.seabattle.domain.entity.CellState
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.usecase.game.EnableClaimUseCase
import com.example.seabattle.domain.usecase.game.EnableReadyUseCase
import com.example.seabattle.domain.usecase.game.ClaimVictoryUseCase
import com.example.seabattle.domain.usecase.game.LeaveGameUseCase
import com.example.seabattle.domain.usecase.game.ListenGameUseCase
import com.example.seabattle.domain.usecase.game.MakeMoveUseCase
import com.example.seabattle.domain.usecase.game.FinishGameUseCase
import com.example.seabattle.domain.usecase.game.UserReadyUseCase
import com.example.seabattle.domain.usecase.user.GetUserProfileUseCase
import com.example.seabattle.domain.usecase.userGames.CancelInvitationUseCase
import com.example.seabattle.domain.repository.SoundManagerRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


class GameViewModel(
    private val sessionService: SessionService,
    private val enableReadyUseCase: EnableReadyUseCase,
    private val userReadyUseCase: UserReadyUseCase,
    private val makeMoveUseCase: MakeMoveUseCase,
    private val listenGameUseCase: ListenGameUseCase,
    private val leaveGameUseCase: LeaveGameUseCase,
    private val cancelInvitationUseCase: CancelInvitationUseCase,
    private val enableClaimUseCase: EnableClaimUseCase,
    private val claimVictoryUseCase: ClaimVictoryUseCase,
    private val finishGameUseCase: FinishGameUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val soundManager: SoundManagerRepo
) : ViewModel(), KoinComponent {
    private val _uiState = MutableStateFlow<GameUiState>(GameUiState())
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _isMuted = MutableStateFlow(soundManager.isMuted)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    // Initialize jobs needed for listening to game updates and checking for AFK
    private var listenGameJob: Job? = null
    private var checkClaimJob: Job? = null
    private var lastClaimDialogDismissalTime: Long = 0L

    init{
        // Initialize the UI state with the current user ID
        viewModelScope.launch {
            val userId = sessionService.getCurrentUserId()
            val userScore = getUserProfileUseCase.invoke().getOrNull()?.score ?: 0
            _uiState.value = _uiState.value.copy(
                userId = userId,
                userScore = userScore
            )
        }
    }

    // This function starts listening to the game updates
    fun startListeningGame() {
        if (listenGameJob?.isActive == true) return
        listenGameJob = viewModelScope.launch {
            val gameId = sessionService.getCurrentGameId()

            if (gameId.isNotEmpty()) {
                listenGameUseCase.invoke(gameId)
                    .collect { result ->
                        result.fold(
                            onSuccess = { collectedGame ->
                                _uiState.value = _uiState.value.copy(game = collectedGame)

                                // Check if the game has started to start checking for AFK
                                if (collectedGame.gameState == GameState.IN_PROGRESS.name && collectedGame.winnerId == null) {
                                    startCheckUserAFK()
                                } else {
                                    cancelCheckUserAFK()
                                }

                                if (collectedGame.gameState == GameState.GAME_FINISHED.name) {
                                    onGameFinished()
                                }
                            },
                            onFailure = { e ->
                                _uiState.value = _uiState.value.copy(error = e)
                            }
                        )
                    }
            }
        }
    }


    // This function checks if the opponent is AFK (Away From Keyboard)
    // and if the conditions to claim victory are met it shows a dialog to the user.
    // This condition are checked every 10 seconds
    private fun startCheckUserAFK() {
        if (checkClaimJob?.isActive == true) return
        checkClaimJob = viewModelScope.launch {
            _uiState.map { it.game }
                .collectLatest { latestGame ->
                    val game = latestGame ?: return@collectLatest
                    detectBoardChanges(game)

                    if (game.gameState != GameState.IN_PROGRESS.name || game.winnerId != null) {
                        _uiState.value = _uiState.value.copy(showClaimDialog = false)
                        return@collectLatest
                    }

                    // Variable to track if the job is still active and the conditions are met to show the dialog
                    while (isActive && game.gameState == GameState.IN_PROGRESS.name && game.winnerId == null) {
                        val claimConditions = enableClaimUseCase.invoke(
                            userId= _uiState.value.userId,
                            game = game
                        )

                        // If the dialog is shown, we store the time of the last dismissal
                        // To avoid showing it to often it will be set a cooldown period
                        val currentTime = System.currentTimeMillis()
                        val timeSinceLastDismiss = currentTime - lastClaimDialogDismissalTime
                        val cooldownMS = 30000L


                        val shouldShowDialog = (claimConditions &&
                                (!_uiState.value.alreadyShownClaimDialog || timeSinceLastDismiss > cooldownMS))


                        // Only update _uiState if the showClaimDialog value actually changes
                        if (_uiState.value.showClaimDialog != shouldShowDialog) {
                            _uiState.value = _uiState.value.copy(showClaimDialog = shouldShowDialog)
                        }

                        // Delay for 10 seconds before checking again
                        // This delay wont cause problems with an update of the game cause the loop will break
                        delay(10000L)
                    }
                }
        }
    }


    // This function cancels the job that checks if the opponent is AFK
    private fun cancelCheckUserAFK() {
        checkClaimJob?.cancel()
        checkClaimJob = null
        _uiState.value = _uiState.value.copy(
            showClaimDialog = false,
            alreadyShownClaimDialog = true
        )
    }


    // This function checks the changes in the game board
    private fun detectBoardChanges(game: Game) {
        val previousPlayer = _uiState.value.previousGame?.currentPlayer
        val previousBoard1 = _uiState.value.previousGame?.boardForPlayer1
        val previousBoard2 = _uiState.value.previousGame?.boardForPlayer2

        // If the previous game is null, we don't have any previous board to compare
        // If the player has changed check both boards
        // If the player has not changed, check only the board of the current player
        if (previousBoard1 != null && previousBoard2 != null) {
            if (previousPlayer != game.currentPlayer) {
                compareBoards(previousBoard1, game.boardForPlayer1)
                compareBoards(previousBoard2, game.boardForPlayer2)
            } else {
                if (game.currentPlayer == game.player1.userId) {
                    compareBoards(previousBoard1, game.boardForPlayer1)
                } else {
                    compareBoards(previousBoard2, game.boardForPlayer2)
                }
            }
        }

        _uiState.value = _uiState.value.copy(previousGame = game)
    }


    // This function compares the previous and current game boards
    private fun compareBoards(previousBoard: Map<String, Map<String, Int>>, currentBoard: Map<String, Map<String, Int>>) {
        for (row in currentBoard.keys) {
            for (col in currentBoard[row]?.keys ?: emptySet()) {
                val previousCell = previousBoard[row]?.get(col)
                val currentCell = currentBoard[row]?.get(col)

                if (previousCell == null || currentCell == null) {
                    continue
                }

                if (previousCell == currentCell) {
                    continue
                }

                val cellState : BasicCellState = CellState.getFromValue(currentCell).toBasic()
                if (cellState == BasicCellState.WATER) {
                    soundManager.playWaterSplash()
                } else if ((cellState == BasicCellState.HIT) || (cellState == BasicCellState.SUNK)) {
                    // When the the ship is SUNK all the cells of the ship are marked as SUNK
                    // So the sound will be played one time for each cell but that's okay
                    soundManager.playShipHit()
                }
            }
        }
    }


    // This function calls the user score use case when the game is finished
    private fun onGameFinished() {
        val game = _uiState.value.game ?: return
        viewModelScope.launch {
            finishGameUseCase.invoke( gameId = game.gameId )
                .onSuccess { score ->
                    _uiState.value = _uiState.value.copy(userScore = score)
                    if (game.winnerId == _uiState.value.userId) {
                        soundManager.playVictory()
                    } else {
                        soundManager.playDefeat()
                    }
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                }
        }
    }


    // This function is called when the user clicks on the "Ready" button
    fun onClickReady() {
        viewModelScope.launch {
            userReadyUseCase.invoke()
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                }
        }
    }


    // This function checks if the "Ready" button should be enabled
    fun enableReadyButton() : Boolean {
        return enableReadyUseCase.invoke(
            userId = _uiState.value.userId,
            game = _uiState.value.game ?: return false
        )
    }


    // This function is called when the user leaves the game
    fun onUserLeave() {
        viewModelScope.launch {
            stopListening() // Stop listening to the game updates before clearing the game

            if (uiState.value.game?.privateGame == true) {
                cancelInvitationUseCase.invoke()
                    .onFailure { e ->
                        _uiState.value = _uiState.value.copy(error = e)
                    }
            }

            leaveGameUseCase.invoke(userId = _uiState.value.userId, game = _uiState.value.game)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(game = null, hasLeftGame = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                }
        }
    }


    // This function is called when the user clicks on a cell in the game board
    fun onClickCell(x: Int, y: Int){
        viewModelScope.launch {
            makeMoveUseCase.invoke(x, y)
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                }
        }
    }


    // This function checks if the user can click on a cell
    fun enableClickCell(gameBoardOwner: String) : Boolean {
        val userId = sessionService.getCurrentUserId()
        if (
            gameBoardOwner == "player1"
            && (userId == uiState.value.game?.player1?.userId)
            && (userId == uiState.value.game?.currentPlayer)
        ) {
            return true
        } else if (
            gameBoardOwner == "player2"
            && (userId == uiState.value.game?.player2?.userId)
            && (userId == uiState.value.game?.currentPlayer)
        ) {
            return true
        }
        else return false
    }


    // This function checks if the user can see the ships positions
    fun enableSeeShips(watcher: String) : Boolean {
        val userId = sessionService.getCurrentUserId()
        return when (watcher) {
            "player1" -> userId == uiState.value.game?.player1?.userId
            "player2" -> userId == uiState.value.game?.player2?.userId
            else -> false
        }
    }


    // This function is called when the user clicks on the "Claim Victory" button
    fun onClaimVictory() {
        viewModelScope.launch {
            claimVictoryUseCase.invoke()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        showClaimDialog = false,
                        alreadyShownClaimDialog = true
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                }
        }
    }


    // This function is called when the user clicks on the "Dismiss Claim Dialog" button
    fun onDismissClaimDialog() {
        lastClaimDialogDismissalTime = System.currentTimeMillis()
        _uiState.value = _uiState.value.copy(
            showClaimDialog = false,
            alreadyShownClaimDialog = true
        )
    }


    // This function is called when the error message is shown to the user
    // It clears the error message from the UI state
    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(error = null)
    }


    // It's used when the user leaves the game screen to clear the jobs
    fun stopListening() {
        listenGameJob?.cancel()
        listenGameJob = null
        checkClaimJob?.cancel()
        checkClaimJob = null
    }


    // Function to mute/unmute the sound
    fun toggleMute() {
        soundManager.toggleMute()
        _isMuted.value = soundManager.isMuted
    }
}