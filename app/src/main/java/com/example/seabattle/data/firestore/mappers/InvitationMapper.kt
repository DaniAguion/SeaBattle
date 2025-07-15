package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.InvitationDto
import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.domain.entity.User


fun InvitationDto.toEntity(): Invitation =
    Invitation(
        gameId = gameId,
        gameName = gameName,
        invitationState = invitationState,
        invitedBy = User(
            userId = invitedBy.userId,
            displayName = invitedBy.displayName,
            photoUrl = invitedBy.photoUrl,
            score = invitedBy.score
        )
    )
