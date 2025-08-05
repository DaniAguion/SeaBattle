package com.example.seabattle.presentation.screens.welcome


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.entity.LoginMethod
import com.example.seabattle.domain.usecase.user.ForgotPassUseCase
import com.example.seabattle.domain.usecase.user.LoginUserUseCase
import com.example.seabattle.domain.usecase.user.RegisterUserUseCase
import com.example.seabattle.presentation.resources.InfoMessages
import com.example.seabattle.presentation.resources.Validator
import com.example.seabattle.presentation.resources.Validator.validateNewPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.onSuccess


class WelcomeViewModel(
    private val loginUseCase: LoginUserUseCase,
    private val registerUseCase: RegisterUserUseCase,
    private val forgotPassUseCase: ForgotPassUseCase,
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

    private fun validateNewPassword(password: String){
        val validationResult = Validator.validateNewPassword(password)
        _uiState.value = _uiState.value.copy(passwordError = validationResult)
    }

    fun onRepeatPasswordUpdate(repeatedPasswordField: String) {
        validateRepeatPassword(repeatedPasswordField)
        _uiState.value = _uiState.value.copy(repeatedPassword = repeatedPasswordField)
    }

    private fun validateRepeatPassword(password: String){
        val validationResult = Validator.validatePassword(password)
        _uiState.value = _uiState.value.copy(repeatedPasswordError = validationResult)
    }

    fun onLoginButtonClicked() {
        validateEmail(_uiState.value.email)
        validatePassword(_uiState.value.password)

        if((uiState.value.emailError != null) || (uiState.value.passwordError != null)){
            _uiState.value = _uiState.value.copy(msgResult = InfoMessages.INVALID_FIELDS)
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
            .onFailure { e ->
                _uiState.value = _uiState.value.copy(error = e)
                _uiState.value = _uiState.value.copy(msgResult = InfoMessages.LOGIN_UNSUCCESSFUL)
                _uiState.value = _uiState.value.copy(isLoggedIn = false)
            }
        }
    }


    fun onClickForgotPassword() {
        validateEmail(_uiState.value.email)

        if (uiState.value.emailError != null){
            _uiState.value = _uiState.value.copy(msgResult = InfoMessages.INVALID_FIELDS)
            return
        }

        viewModelScope.launch {
            forgotPassUseCase.invoke(_uiState.value.email)
                .onSuccess {
                    _uiState.value =
                        _uiState.value.copy(msgResult = InfoMessages.PASSWORD_RESET_SENT)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e,
                        msgResult = InfoMessages.PASSWORD_RESET_FAILED
                    )
                }
        }
    }


    fun onRegisterButtonClicked() {
        validateUsername(_uiState.value.username)
        validateEmail(_uiState.value.email)
        validateNewPassword(_uiState.value.password)
        validateRepeatPassword(_uiState.value.repeatedPassword)


        if ((uiState.value.usernameError != null) ||
            (uiState.value.emailError != null) ||
            (uiState.value.passwordError != null) ||
            (uiState.value.repeatedPasswordError != null)
        ){
            _uiState.value = _uiState.value.copy(msgResult = InfoMessages.INVALID_FIELDS)
            return
        }

        if (_uiState.value.password != _uiState.value.repeatedPassword) {
            _uiState.value = _uiState.value.copy(msgResult = InfoMessages.PASSWORDS_DOES_NOT_MATCH)
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
            .onFailure { e ->
                _uiState.value = _uiState.value.copy(error = e)
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
            .onFailure { e ->
                _uiState.value = _uiState.value.copy(error = e)
                _uiState.value = _uiState.value.copy(msgResult = InfoMessages.LOGIN_UNSUCCESSFUL)
                _uiState.value = _uiState.value.copy(isLoggedIn = false)
            }
        }
    }

    fun onResetError() {
        _uiState.value = _uiState.value.copy(
            usernameError = null,
            emailError = null,
            passwordError = null,
            repeatedPasswordError = null,
            error = null,
            msgResult = null
        )
    }

    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(error = null)
    }
}
