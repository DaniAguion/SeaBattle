package com.example.seabattle.data.storage

import android.content.SharedPreferences

class SecurePrefsData(private val sharedPrefs: SharedPreferences) {

    companion object {
        private const val KEY_TOKEN = "user_token"
        private const val KEY_NAME = "user_name"
        private const val KEY_AVATAR = "user_avatar"
    }

    fun saveUserSession(token: String, name: String, avatar: String) {
        sharedPrefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_NAME, name)
            .putString(KEY_AVATAR, avatar)
            .apply()
    }

    fun getToken(): String? = sharedPrefs.getString(KEY_TOKEN, null)

    fun getUserName(): String? = sharedPrefs.getString(KEY_NAME, null)

    fun getAvatar(): String? = sharedPrefs.getString(KEY_AVATAR, null)

    fun clearSession() {
        sharedPrefs.edit().clear().apply()
    }
}