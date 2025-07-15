package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.UserDto
import com.example.seabattle.domain.entity.User


fun User.toDto(): UserDto =
    UserDto(
        userId = userId,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        status = status,
        score = score,
        history = history.map { it.toDto() }
    )


fun UserDto.toEntity(): User =
    User(
        userId = userId,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        status = status,
        score = score,
        history = history.map { it.toEntity() }
    )
