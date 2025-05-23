package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.UserDto
import com.example.seabattle.domain.entity.User



fun User.toDto(): UserDto =
    UserDto(
        userId = userId,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        score = score
    )


fun UserDto.toEntity(): User =
    User(
        userId = userId,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        score = score
    )
