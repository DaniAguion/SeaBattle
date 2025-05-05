package com.example.seabattle.data.local

import android.content.SharedPreferences
import com.example.seabattle.domain.entity.User
import androidx.core.content.edit

class SecurePrefsData(private val sharedPrefs: SharedPreferences) {

    fun saveUserSession(user : User?) {
        if (user == null) return
        sharedPrefs.edit {
            putString("key_uid", user.userId)
            putString("key_name", user.displayName)
            putString("key_email", user.email)
            putString("key_photo", user.photoUrl)
        }
    }

    fun clearSession() {
        sharedPrefs.edit() { clear() }
    }

    fun getUid(): String = sharedPrefs.getString("key_uid", "") ?: ""

    fun getDisplayName(): String = sharedPrefs.getString("key_name", "")  ?: ""

    fun getEmail(): String = sharedPrefs.getString("key_email", "")  ?: ""

    fun getPhoto(): String = sharedPrefs.getString("key_photo", "")  ?: ""
}