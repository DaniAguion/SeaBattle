package com.example.seabattle.ui

import androidx.lifecycle.ViewModel
import com.example.seabattle.domain.auth.usecase.LoginUserUseCase
import com.example.seabattle.domain.auth.usecase.LogoutUserUseCase
import com.example.seabattle.domain.auth.usecase.NewSessionUseCase

class HomeViewModel(
    private val newSessionUseCase: NewSessionUseCase
) : ViewModel() {

}