package com.example.seabattle.presentation.screens.welcome


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.entity.LoginMethod
import com.example.seabattle.domain.usecase.auth.LoginUserUseCase
import com.example.seabattle.domain.usecase.auth.RegisterUserUseCase
import com.example.seabattle.presentation.validation.Validator
import com.example.seabattle.presentation.validation.Validator.validateNewPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.onSuccess


class WelcomeViewModel(
    private val loginUseCase: LoginUserUseCase,
    private val registerUseCase: RegisterUserUseCase,
    private val googleSignIn: GoogleSignIn
) : ViewModel() {
    private val _uiState = MutableStateFlow(WelcomeUiState())
    var uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    fun onUsernameUpdate(usernameField: String) {
        validateUsername(usernameField)
        _uiState.value = _uiState.value.copy(username = usernameField)
    }

    private fun validateUsername(username: String){
        val validationResult = Validator.validateUsername(username)
        _uiState.value = _uiState.value.copy(usernameError = validationResult)
    }

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

    fun onLoginButtonClicked() {
        validateEmail(_uiState.value.email)
        validatePassword(_uiState.value.password)

        if((uiState.value.emailError != null) || (uiState.value.passwordError != null)){
            _uiState.value = _uiState.value.copy(msgResult = InfoMessages.LOGIN_UNSUCCESSFUL)
            return
        }

        viewModelScope.launch {
            loginUseCase(
                LoginMethod.EmailPassword(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
            )
            .onSuccess { result ->
                if (result) {
                    _uiState.value = _uiState.value.copy(msgResult = InfoMessages.LOGIN_SUCCESSFUL)
                    _uiState.value = _uiState.value.copy(isLoggedIn = true)
                } else {
                    _uiState.value = _uiState.value.copy(msgResult = InfoMessages.LOGIN_UNSUCCESSFUL)
                    _uiState.value = _uiState.value.copy(isLoggedIn = false)
                }
            }
            .onFailure { throwable ->
                val errorMessage = throwable.message
                _uiState.value = _uiState.value.copy(errorMessage = errorMessage)
                _uiState.value = _uiState.value.copy(msgResult = InfoMessages.LOGIN_UNSUCCESSFUL)
                _uiState.value = _uiState.value.copy(isLoggedIn = false)
            }
        }
    }

    fun onRegisterButtonClicked() {
        validateUsername(_uiState.value.username)
        validateEmail(_uiState.value.email)
        validateNewPassword(_uiState.value.password)

        if ((uiState.value.usernameError != null) ||
            (uiState.value.emailError != null) ||
            (uiState.value.passwordError != null)
        ){
            _uiState.value = _uiState.value.copy(msgResult = InfoMessages.REGISTER_UNSUCCESSFUL)
            return
        }

        viewModelScope.launch {
            registerUseCase(
                username = _uiState.value.username,
                email = _uiState.value.email,
                password = _uiState.value.password
            )
            .onSuccess { result ->
                if (result) {
                    _uiState.value = _uiState.value.copy(msgResult = InfoMessages.REGISTER_SUCCESSFUL)
                    _uiState.value = _uiState.value.copy(isLoggedIn = true)
                } else {
                    _uiState.value = _uiState.value.copy(msgResult = InfoMessages.REGISTER_UNSUCCESSFUL)
                    _uiState.value = _uiState.value.copy(isLoggedIn = false)
                }
            }
            .onFailure { throwable ->
                val errorMessage = throwable.message
                _uiState.value = _uiState.value.copy(errorMessage = errorMessage)
                _uiState.value = _uiState.value.copy(msgResult = InfoMessages.REGISTER_UNSUCCESSFUL)
                _uiState.value = _uiState.value.copy(isLoggedIn = false)
            }
        }
    }

    fun onGoogleButtonClicked(context: Context) {
        viewModelScope.launch {
            val googleIdToken = googleSignIn.signIn(context) ?: ""

            loginUseCase(
                LoginMethod.Google(
                    googleIdToken = googleIdToken
                )
            )
            .onSuccess { result ->
                if (result) {
                    _uiState.value = _uiState.value.copy(msgResult = InfoMessages.LOGIN_SUCCESSFUL)
                    _uiState.value = _uiState.value.copy(isLoggedIn = true)
                } else {
                    _uiState.value = _uiState.value.copy(msgResult = InfoMessages.LOGIN_UNSUCCESSFUL)
                    _uiState.value = _uiState.value.copy(isLoggedIn = false)
                }
            }
            .onFailure { throwable ->
                val errorMessage = throwable.message
                _uiState.value = _uiState.value.copy(errorMessage = errorMessage)
                _uiState.value = _uiState.value.copy(msgResult = InfoMessages.LOGIN_UNSUCCESSFUL)
                _uiState.value = _uiState.value.copy(isLoggedIn = false)
            }
        }
    }

    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
