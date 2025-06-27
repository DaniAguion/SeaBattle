package com.example.seabattle.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.usecase.game.CreateGameUseCase
import com.example.seabattle.domain.usecase.game.GetGamesUseCase
import com.example.seabattle.domain.usecase.game.JoinGameUseCase
import com.example.seabattle.presentation.validation.Validator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class HomeViewModel(
    private val createGameUseCase: CreateGameUseCase,
    private val getGamesUseCase: GetGamesUseCase,
    private val joinGameUseCase: JoinGameUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(gamesList = emptyList()))
    var uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Active listener use get the updated game list
    private var getGamesJob: Job? = null


    fun startListeningList() {
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
                                errorMessage = e.message
                            )
                        }
                }
        }
    }


    fun stopListeningList() {
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
            createGameUseCase.invoke(gameName = gameName)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(hasJoined = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(errorMessage = e.message)
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
                    _uiState.value = _uiState.value.copy(errorMessage = e.message)
                }
        }
    }


    fun resetUiState() {
        _uiState.value = _uiState.value.copy(
            hasJoined = false,
            errorMessage = null
        )
    }


    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}