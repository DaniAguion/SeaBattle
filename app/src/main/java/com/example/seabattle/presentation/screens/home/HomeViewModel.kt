package com.example.seabattle.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.domain.usecase.game.CreateGameUseCase
import com.example.seabattle.domain.usecase.game.GetGamesUseCase
import com.example.seabattle.domain.usecase.game.JoinGameUseCase
import com.example.seabattle.domain.usecase.user.FindPlayerUseCase
import com.example.seabattle.domain.usecase.userGames.InviteUserUseCase
import com.example.seabattle.domain.usecase.userGames.ListenUserGamesUseCase
import com.example.seabattle.domain.usecase.userGames.RejectInvitationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class HomeViewModel(
    private val createGameUseCase: CreateGameUseCase,
    private val findPlayerUseCase: FindPlayerUseCase,
    private val inviteUserUseCase: InviteUserUseCase,
    private val getGamesUseCase: GetGamesUseCase,
    private val joinGameUseCase: JoinGameUseCase,
    private val listenUserGamesUseCase: ListenUserGamesUseCase,
    private val rejectInvitationUseCase: RejectInvitationUseCase,
    private val sessionService: SessionService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(gamesList = emptyList()))
    var uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Active listener use get the updated game list
    private var getUserGamesJob: Job? = null
    private var getGamesJob: Job? = null


    fun startListeningUserGames(){
        getUserGamesJob = viewModelScope.launch {
            listenUserGamesUseCase.invoke()
                .collect { result ->
                    result
                        .onSuccess { userGames ->
                            if (userGames.currentGameId.isNullOrEmpty()) {
                                sessionService.clearCurrentGame()
                                _uiState.value = _uiState.value.copy(hasJoined = false)
                            } else {
                                sessionService.setCurrentGameId(userGames.currentGameId)
                                _uiState.value = _uiState.value.copy(hasJoined = true)
                            }
                            _uiState.value = _uiState.value.copy(invitationsList = userGames.gamesInvitations)
                        }
                        .onFailure { e ->
                            _uiState.value = _uiState.value.copy(
                                error = e
                            )
                        }
                }
        }
    }


    fun startGettingGames() {
        _uiState.value = _uiState.value.copy(loadingGamesList = true)
        // Start listening to fetch games
        getGamesJob = viewModelScope.launch {
            getGamesUseCase.invoke()
                . collect { result ->
                    result
                        .onSuccess { games ->
                            _uiState.value = _uiState.value.copy(
                                gamesList = games,
                                errorGameList = false,
                                loadingGamesList = false
                            )
                        }
                        .onFailure { e ->
                            _uiState.value = _uiState.value.copy(
                                gamesList = emptyList(),
                                errorGameList = true,
                                loadingGamesList = false,
                                error = e
                            )
                        }
                }
        }
    }


    fun startListeners(){
        startListeningUserGames()
        startGettingGames()
    }


    fun stopListeners() {
        getUserGamesJob?.cancel()
        getUserGamesJob = null
        getGamesJob?.cancel()
        getGamesJob = null
    }


    fun onClickCreateGame() {
        viewModelScope.launch {
            createGameUseCase.invoke(privateGame = false)
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                }
        }
    }


    fun onUserSearchChange(searchText: String) {
        _uiState.value = _uiState.value.copy(
            searchedUser = searchText,
            playersList = emptyList(),
            searchDone = false,
            loadingPlayersList = false,
            errorPlayersList = false,
        )
    }


    fun onUserSearch() {
        if (uiState.value.searchedUser.isEmpty()) return

        _uiState.value = _uiState.value.copy(loadingPlayersList = true)

        viewModelScope.launch {
            findPlayerUseCase.invoke(uiState.value.searchedUser)
                .onSuccess { players ->
                    _uiState.value = _uiState.value.copy(
                        playersList = players,
                        searchDone = true,
                        loadingPlayersList = false,
                        errorPlayersList = false
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        playersList = emptyList(),
                        searchDone = false,
                        loadingPlayersList = false,
                        errorPlayersList = true,
                        error = e
                    )
                }
        }
    }


    fun onClickInviteUser(invitedPlayerId: String) {
        if (invitedPlayerId.isEmpty() || invitedPlayerId == sessionService.getCurrentUserId()) return

        viewModelScope.launch {
            createGameUseCase.invoke(privateGame = true)
                .onSuccess { gameId ->
                    inviteUserUseCase.invoke(invitedPlayerId, gameId)
                        .onFailure { e ->
                            _uiState.value = _uiState.value.copy(error = e)
                        }
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                }

        }
    }



    fun onClickJoinGame(gameId: String) {
        viewModelScope.launch {
            joinGameUseCase.invoke(gameId)
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                }
        }
    }


    fun onClickRejectInvitation(invitation: Invitation) {
        viewModelScope.launch {
            rejectInvitationUseCase.invoke(invitation)
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                }
        }
    }


    fun resetUiState() {
        _uiState.value = _uiState.value.copy(
            hasJoined = false,
            error = null
        )
    }


    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(error = null)
    }
}