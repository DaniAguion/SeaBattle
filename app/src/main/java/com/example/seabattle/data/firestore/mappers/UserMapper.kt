package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.entities.UserProfileEntity
import com.example.seabattle.domain.model.UserProfile


fun UserProfileEntity.toDomainModel(): UserProfile =
    UserProfile(
        userId      = userId,
        displayName = displayName,
        email       = email,
        photoUrl    = photoUrl
    )

fun UserProfile.toEntity(): UserProfileEntity =
    UserProfileEntity(
        userId      = userId,
        displayName = displayName,
        email       = email,
        photoUrl    = photoUrl,
        score       = 0,
        isOnline    = false,
        lookingForGame = false,
        inGame      = false
    )
