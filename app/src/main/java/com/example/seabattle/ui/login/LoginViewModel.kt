package com.example.seabattle.ui.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.data.repository.AuthRepositoryImpl
import com.example.seabattle.validation.ValidationError
import com.example.seabattle.validation.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoginViewModel(private val authRepository: AuthRepositoryImpl) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    var uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

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


    fun onSignInButtonClicked() {
        validateEmail(_uiState.value.email)
        validatePassword(_uiState.value.password)

        if((uiState.value.emailError != null) || (uiState.value.passwordError != null)){
            _uiState.value = _uiState.value.copy(loginResult = LoginMsgs.LOGIN_UNSUCCESSFUL)
            return
        }

        viewModelScope.launch {
            val tryResult = authRepository.loginUser(
                email = _uiState.value.email,
                password = _uiState.value.password
            )
            val loginResult = if (tryResult) LoginMsgs.LOGIN_SUCCESSFUL else LoginMsgs.LOGIN_UNSUCCESSFUL
            _uiState.value = _uiState.value.copy(loginResult = loginResult)
        }
    }


}
