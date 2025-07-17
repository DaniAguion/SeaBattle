package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.PlayerDto
import com.example.seabattle.domain.entity.Player


fun Player.toDto(): PlayerDto =
    PlayerDto(
        userId = userId,
        displayName = displayName,
        photoUrl = photoUrl,
        status = status,
        score = score
    )


fun PlayerDto.toEntity(): Player =
    Player(
        userId = userId,
        displayName = displayName,
        photoUrl = photoUrl,
        status = status,
        score = score
    )
