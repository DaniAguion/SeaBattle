package com.example.seabattle.presentation.screens.profile

import com.example.seabattle.domain.entity.User
import com.example.seabattle.presentation.resources.InfoMessages
import com.example.seabattle.presentation.resources.ValidationError

data class ProfileUiState(
    val userLoggedIn: Boolean = true,
    val user: User = User(),
    val userNameField: String = "",
    val userNameError: ValidationError? = null,
    val email: String = "",
    val errorList: Boolean = false,
    val loadingList: Boolean = true,
    val deleteDialog: Boolean = false,
    val deleteConfirmationText: String = "",
    val msgResult: InfoMessages? = null,
    val error: Throwable? = null,
)