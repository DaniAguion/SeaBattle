package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.InvitationDto
import com.example.seabattle.domain.entity.Invitation


fun InvitationDto.toEntity(): Invitation =
    Invitation(
        gameId = gameId,
        gameName = gameName,
        invitedBy = invitedBy.toEntity()
    )

fun Invitation.toDto(): InvitationDto =
    InvitationDto(
        gameId = gameId,
        gameName = gameName,
        invitedBy = invitedBy.toDto()
    )
