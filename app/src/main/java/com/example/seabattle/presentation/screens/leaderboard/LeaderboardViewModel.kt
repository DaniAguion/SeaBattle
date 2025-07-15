package com.example.seabattle.presentation.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.entity.BasicPlayer
import com.example.seabattle.domain.usecase.leaderboard.GetLeaderboardUseCase
import com.example.seabattle.domain.usecase.leaderboard.GetUserPositionUseCase
import com.example.seabattle.domain.usecase.user.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserPosition: GetUserPositionUseCase,
    private val getLeaderboardUseCase : GetLeaderboardUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LeaderboardUiState(usersList = emptyList()))
    var uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()


    fun getUserPosition() {
        viewModelScope.launch {
            getUserProfileUseCase.invoke()
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        user = BasicPlayer(
                            userId = user.userId,
                            displayName = user.displayName,
                            photoUrl = user.photoUrl,
                            score = user.score
                        ),
                        error = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        user = null,
                        error = e
                    )
                }
        }
        viewModelScope.launch {
            getUserPosition.invoke()
                .onSuccess { position ->
                    _uiState.value = _uiState.value.copy(
                        userPosition = position,
                        error = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        userPosition = null,
                        error = e
                    )
                }
        }
    }


    fun getLeaderboard() {
        viewModelScope.launch {
            getLeaderboardUseCase.invoke()
                .onSuccess { usersList ->
                    _uiState.value = _uiState.value.copy(
                        usersList = usersList,
                        errorList = false,
                        loadingList = false
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        usersList = emptyList(),
                        errorList = true,
                        loadingList = false,
                        error = e
                    )
                }
        }
    }


    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(error = null)
    }
}