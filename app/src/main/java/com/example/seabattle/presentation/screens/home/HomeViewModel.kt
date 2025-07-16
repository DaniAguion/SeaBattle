package com.example.seabattle.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.usecase.game.CreateGameUseCase
import com.example.seabattle.domain.usecase.game.GetGamesUseCase
import com.example.seabattle.domain.usecase.game.JoinGameUseCase
import com.example.seabattle.domain.usecase.userGames.ListenUserGamesUseCase
import com.example.seabattle.presentation.resources.Validator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class HomeViewModel(
    private val createGameUseCase: CreateGameUseCase,
    private val getGamesUseCase: GetGamesUseCase,
    private val joinGameUseCase: JoinGameUseCase,
    private val listenUserGamesUseCase: ListenUserGamesUseCase,
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
        _uiState.value = _uiState.value.copy(loadingList = true)
        // Start listening to fetch games
        getGamesJob = viewModelScope.launch {
            getGamesUseCase.invoke()
                . collect { result ->
                    result
                        .onSuccess { games ->
                            _uiState.value = _uiState.value.copy(
                                gamesList = games,
                                errorList = false,
                                loadingList = false
                            )
                        }
                        .onFailure { e ->
                            _uiState.value = _uiState.value.copy(
                                gamesList = emptyList(),
                                errorList = true,
                                loadingList = false,
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


    fun onGameNameUpdate(gameName: String) {
        val validationResult = Validator.validateGameName(gameName)
        _uiState.value = _uiState.value.copy(gameNameError = validationResult)
        _uiState.value = _uiState.value.copy(gameName = gameName)
    }


    fun onClickCreateGame(gameName: String) {
        onGameNameUpdate(gameName = gameName)
        if (uiState.value.gameNameError != null){
            return
        }
        viewModelScope.launch {
            createGameUseCase.invoke(gameName = gameName, privateGame = false)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(hasJoined = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                }
        }
    }


    fun onClickJoinGame(gameId: String) {
        viewModelScope.launch {
            joinGameUseCase.invoke(gameId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(hasJoined = true)
                }
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