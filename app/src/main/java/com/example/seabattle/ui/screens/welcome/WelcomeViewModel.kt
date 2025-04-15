package com.example.seabattle.ui.screens.welcome


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.auth.LoginMethod
import com.example.seabattle.domain.auth.usecase.LoginUserUseCase
import com.example.seabattle.domain.auth.usecase.RegisterUserUseCase
import com.example.seabattle.domain.validation.Validator
import com.example.seabattle.domain.validation.Validator.validateNewPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class WelcomeViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val googleSignIn: GoogleSignIn
) : ViewModel() {
    private val _uiState = MutableStateFlow(WelcomeUiState())
    var uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

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
            _uiState.value = _uiState.value.copy(msgResult = InfoMsgs.LOGIN_UNSUCCESSFUL)
            return
        }

        viewModelScope.launch {
            val tryResult = loginUserUseCase(
                LoginMethod.EmailPassword(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
            )
            val loginResult = if (tryResult) InfoMsgs.LOGIN_SUCCESSFUL else InfoMsgs.LOGIN_UNSUCCESSFUL
            _uiState.value = _uiState.value.copy(msgResult = loginResult)
            _uiState.value = _uiState.value.copy(isLoggedIn = tryResult)
        }
    }

    fun onRegisterButtonClicked() {
        validateEmail(_uiState.value.email)
        validateNewPassword(_uiState.value.password)

        if((uiState.value.emailError != null) || (uiState.value.passwordError != null)){
            _uiState.value = _uiState.value.copy(msgResult = InfoMsgs.REGISTER_UNSUCCESSFUL)
            return
        }

        viewModelScope.launch {
            val tryResult = registerUserUseCase(
                email = _uiState.value.email,
                password = _uiState.value.password
            )
            val registerResult = if (tryResult) InfoMsgs.REGISTER_SUCCESSFUL else InfoMsgs.REGISTER_UNSUCCESSFUL
            _uiState.value = _uiState.value.copy(msgResult = registerResult)
            _uiState.value = _uiState.value.copy(isLoggedIn = tryResult)
        }
    }

    fun onGoogleButtonClicked(context: Context) {
        viewModelScope.launch {
            val googleIdToken = googleSignIn.signIn(context) ?: ""

            val tryResult = loginUserUseCase(
                LoginMethod.Google(
                    googleIdToken = googleIdToken
                )
            )
            val loginResult = if (tryResult) InfoMsgs.LOGIN_SUCCESSFUL else InfoMsgs.LOGIN_UNSUCCESSFUL
            _uiState.value = _uiState.value.copy(msgResult = loginResult)
            _uiState.value = _uiState.value.copy(isLoggedIn = tryResult)
        }
    }
}
