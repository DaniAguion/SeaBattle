package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.UserDto
import com.example.seabattle.domain.entity.User


fun User.toUserDto(): UserDto =
    UserDto(
        userId = userId,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        status = status,
        score = score
    )


fun UserDto.toUserEntity(): User =
    User(
        userId = userId,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        status = status,
        score = score
    )
