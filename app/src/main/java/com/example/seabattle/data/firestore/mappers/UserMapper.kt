package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.entities.UserEntity
import com.example.seabattle.domain.model.User

// At the moment, the UserEntity and User classes are identical.

fun UserEntity.toDomainModel(): User =
    User(
        userId      = userId,
        displayName = displayName,
        email       = email,
        photoUrl    = photoUrl,
        score       = score,
        online      = online,
        lookingForGame = lookingForGame,
        inGame      = inGame
    )

fun User.toEntity(): UserEntity =
    UserEntity(
        userId      = userId,
        displayName = displayName,
        email       = email,
        photoUrl    = photoUrl,
        score       = score,
        online      = online,
        lookingForGame = lookingForGame,
        inGame      = inGame
    )
