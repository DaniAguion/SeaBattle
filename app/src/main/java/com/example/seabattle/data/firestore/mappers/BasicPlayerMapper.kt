package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.BasicPlayerDto
import com.example.seabattle.domain.entity.BasicPlayer


fun BasicPlayer.toDto(): BasicPlayerDto =
    BasicPlayerDto(
        userId = userId,
        displayName = displayName,
        photoUrl = photoUrl,
        score = score
    )


fun BasicPlayerDto.toEntity(): BasicPlayer =
    BasicPlayer(
        userId = userId,
        displayName = displayName,
        photoUrl = photoUrl,
        score = score
    )
