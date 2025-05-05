package com.example.seabattle.domain.entity

sealed class LoginMethod {
    data class EmailPassword(val email: String, val password: String) : LoginMethod()
    data class Google(val googleIdToken: String) : LoginMethod()
}