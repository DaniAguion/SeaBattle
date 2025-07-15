package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.BasicUserDto
import com.example.seabattle.data.firestore.dto.InvitationDto
import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.domain.entity.User


fun InvitationDto.toEntity(): Invitation =
    Invitation(
        gameId = gameId,
        gameName = gameName,
        invitedBy = User(
            userId = invitedBy.userId,
            displayName = invitedBy.displayName,
            photoUrl = invitedBy.photoUrl,
            score = invitedBy.score
        )
    )

fun Invitation.toDto(): InvitationDto =
    InvitationDto(
        gameId = gameId,
        gameName = gameName,
        invitedBy = BasicUserDto(
            userId = invitedBy.userId,
            displayName = invitedBy.displayName,
            photoUrl = invitedBy.photoUrl,
            score = invitedBy.score
        )
    )
