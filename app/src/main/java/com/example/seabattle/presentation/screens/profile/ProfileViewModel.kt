package com.example.seabattle.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.usecase.user.ChangeUserNameUseCase
import com.example.seabattle.domain.usecase.user.DeleteUserUseCase
import com.example.seabattle.domain.usecase.user.GetUserProfileUseCase
import com.example.seabattle.domain.usecase.user.ListenUserUseCase
import com.example.seabattle.domain.usecase.user.LogoutUserUseCase
import com.example.seabattle.presentation.resources.InfoMessages
import com.example.seabattle.presentation.resources.Validator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.onFailure


class ProfileViewModel(
    private val logoutUseCase: LogoutUserUseCase,
    private val listenUserUseCase: ListenUserUseCase,
    private val getUserProfile: GetUserProfileUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val changeUserName: ChangeUserNameUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState())
    var uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Listeners use to observe the game updates
    private var updateUIJob: Job? = null



    fun startListening() {
        updateUIJob = viewModelScope.launch {
            listenUserUseCase.invoke()
                .collect { userId ->
                    if (userId != null) {
                        _uiState.value = ProfileUiState(userLoggedIn = true)
                    } else {
                        _uiState.value = ProfileUiState(userLoggedIn = false)
                    }
                }
        }
    }


    // Function to load the user history
    fun loadUserProfile() {
        viewModelScope.launch {
            getUserProfile.invoke()
                .onSuccess { userProfile ->
                    _uiState.value = _uiState.value.copy(
                        user = userProfile,
                        userNameField = userProfile.displayName,
                        loadingList = false,
                        errorList = false
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e,
                        loadingList = false,
                        errorList = true
                    )
                }
        }
    }


    // Function to handle logout button click
    fun onLogoutButtonClicked() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    // Function to handle username field update
    fun onUsernameUpdate(usernameField: String) {
        validateUsername(usernameField)
        _uiState.value = _uiState.value.copy(userNameField = usernameField)
    }

    // Function to validate the username
    private fun validateUsername(username: String){
        val validationResult = Validator.validateUsername(username)
        _uiState.value = _uiState.value.copy(userNameError = validationResult)
    }


    // Function to handle change username button click
    fun onChangeUsernameClicked(){
        validateUsername(_uiState.value.userNameField)
        if (_uiState.value.userNameError != null) {
            _uiState.value = _uiState.value.copy(msgResult = InfoMessages.INVALID_USERNAME)
            return
        }

        viewModelScope.launch {
            changeUserName.invoke(uiState.value.userNameField)
                .onSuccess { result ->
                    _uiState.value = _uiState.value.copy(msgResult = InfoMessages.CHANGE_SUCCESSFUL)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                    _uiState.value = _uiState.value.copy(msgResult = InfoMessages.CHANGE_UNSUCCESSFUL)
                }
        }
    }


    // Function to handle delete account button click
    fun onDeleteAccountClicked() {
        _uiState.value = _uiState.value.copy(deleteDialog = true)
    }


    // Function to handle cancel button click in the delete dialog
    fun onClickCancel() {
        _uiState.value = _uiState.value.copy(deleteDialog = false)
    }


    // Function to handle confirm button click in the delete dialog
    fun onClickConfirm() {
        viewModelScope.launch {
            deleteUserUseCase.invoke()
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e)
                }
        }
        _uiState.value = _uiState.value.copy(deleteDialog = false)
    }


    fun stopListening() {
        updateUIJob?.cancel()
        updateUIJob = null
    }


    // Function to delete confirmation text
    fun onDeleteConfirmTextUpdated(textField: String) {
        _uiState.value = _uiState.value.copy(deleteConfirmationText = textField)
    }


    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(error = null)
    }
}