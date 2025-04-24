package com.example.seabattle.data.storage

import android.content.SharedPreferences
import com.example.seabattle.domain.model.UserProfile
import androidx.core.content.edit

class SecurePrefsData(private val sharedPrefs: SharedPreferences) {

    companion object {
        private const val KEY_UID = ""
        private const val KEY_NAME = ""
        private const val KEY_EMAIL = ""
        private const val KEY_PHOTO = ""
    }

    fun saveUserSession(userProfile : UserProfile?) {
        if (userProfile == null) return
        sharedPrefs.edit {
            putString("key_uid", userProfile.userId)
            putString("key_name", userProfile.displayName)
            putString("key_email", userProfile.email)
            putString("key_photo", userProfile.photoUrl)
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