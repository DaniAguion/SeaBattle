package com.example.seabattle.data.repository

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
            null
        }
    }
}