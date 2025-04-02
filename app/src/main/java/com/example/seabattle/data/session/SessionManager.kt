package com.example.seabattle.data.session

import com.example.seabattle.data.storage.SecurePrefsData

class SessionManager(
    private val securePrefs: SecurePrefsData
) {
    fun login(token: String, name: String, avatar: String) {
        securePrefs.saveUserSession(token, name, avatar)
    }

    fun logout() {
        securePrefs.clearSession()
    }

    fun getUserName(): String? = securePrefs.getUserName()
    fun getAvatar(): String? = securePrefs.getAvatar()
    fun getToken(): String? = securePrefs.getToken()

    fun isLoggedIn(): Boolean = getToken() != null
}