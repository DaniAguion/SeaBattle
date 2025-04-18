package com.example.seabattle.data.repository

import android.util.Log
import com.example.seabattle.domain.firestore.repository.FirestoreRepository
import com.example.seabattle.domain.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepositoryImpl(
    private val db: FirebaseFirestore
) : FirestoreRepository {
    override suspend fun createUserProfile(userId: String, userProfile: UserProfile): Boolean {
        return try {
            db.collection("users").document(userId).set(userProfile).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error creating user profile: ${e.message}")
            false
        }
    }

    override suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val document = db.collection("users").document(userId).get().await()
            if (document.exists()) {
                document.toObject(UserProfile::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting user profile: ${e.message}")
            null
        }
    }
}