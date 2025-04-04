package com.example.seabattle.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.auth.usecase.RegisterUserUseCase
import com.example.seabattle.domain.validation.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    var uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onEmailUpdate(emailField: String) {
        validateEmail(emailField)
        _uiState.value = _uiState.value.copy(email = emailField)
    }

    private fun validateEmail(email: String){
        val validationResult = Validator.validateEmail(email)
        _uiState.value = _uiState.value.copy(emailError = validationResult)
    }

    fun onPasswordUpdate(passwordField: String) {
        validatePassword(passwordField)
        _uiState.value = _uiState.value.copy(password = passwordField)
    }

    private fun validatePassword(password: String){
        val validationResult = Validator.validatePassword(password)
        _uiState.value = _uiState.value.copy(passwordError = validationResult)
    }


    fun onRegisterButtonClicked() {
        validateEmail(_uiState.value.email)
        validatePassword(_uiState.value.password)

        if((uiState.value.emailError != null) || (uiState.value.passwordError != null)){
            _uiState.value = _uiState.value.copy(registerResult = RegisterMsgs.REGISTER_SUCCESSFUL)
            return
        }

        viewModelScope.launch {
            val tryResult = registerUserUseCase(
                email = _uiState.value.email,
                password = _uiState.value.password
            )
            val registerResult = if (tryResult) RegisterMsgs.REGISTER_SUCCESSFUL else RegisterMsgs.REGISTER_UNSUCCESSFUL
            _uiState.value = _uiState.value.copy(registerResult = registerResult)
        }
    }
}
