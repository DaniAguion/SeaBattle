package com.example.seabattle.data.storage

import android.content.SharedPreferences
import com.example.seabattle.domain.model.UserProfile
import androidx.core.content.edit

class SecurePrefsData(private val sharedPrefs: SharedPreferences) {

    companion object {
        private const val KEY_UID = "-1"
        private const val KEY_NAME = ""
        private const val KEY_EMAIL = ""
        private const val KEY_PHOTO = ""
    }

    fun saveUserSession(userProfile : UserProfile?) {
        if (userProfile == null) {
            return
        }
        sharedPrefs.edit() {
            putString(KEY_UID, userProfile.uid)
                .putString(KEY_NAME, userProfile.displayName)
                .putString(KEY_EMAIL, userProfile.email)
                .putString(KEY_PHOTO, userProfile.photoUrl)
        }
    }

    fun clearSession() {
        sharedPrefs.edit() { clear() }
    }

    fun getUid(): String = sharedPrefs.getString(KEY_UID, "-1") ?: "-1"

    fun getDisplayName(): String = sharedPrefs.getString(KEY_NAME, "")  ?: ""

    fun getEmail(): String = sharedPrefs.getString(KEY_EMAIL, "")  ?: ""

    fun getPhoto(): String = sharedPrefs.getString(KEY_PHOTO, "")  ?: ""
}