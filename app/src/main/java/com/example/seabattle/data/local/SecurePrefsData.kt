package com.example.seabattle.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.seabattle.domain.entity.User


class SecurePrefsData(private val sharedPrefs: SharedPreferences) {
    // SecurePrefsData is used to store user session as encrypted data.
    // It is configured in the app dependency injection module.


    fun saveUserSession(user : User?) {
        if (user == null) return

        sharedPrefs.edit {
            putString("key_uid", user.userId)
            putString("key_name", user.displayName)
            putString("key_email", user.email)
            putString("key_photo", user.photoUrl)
            putString("key_score", user.score.toString())
        }
    }


    fun getUserSession(): User? {
        // If userId is null, it means that the user is not logged in.
        val userId = sharedPrefs.getString("key_uid", null)
        if (userId == null) return null

        return User(
            userId = sharedPrefs.getString("key_uid", "") ?: "",
            displayName = sharedPrefs.getString("key_name", "") ?: "",
            email = sharedPrefs.getString("key_email", "") ?: "",
            photoUrl = sharedPrefs.getString("key_photo", "") ?: "",
            score = sharedPrefs.getString("key_score", "0")?.toInt() ?: 0
        )
    }


    fun clearUserSession() {
        sharedPrefs.edit() { clear() }
    }
}