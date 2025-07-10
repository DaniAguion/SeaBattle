package com.example.seabattle.presentation.resources

import androidx.annotation.StringRes
import com.example.seabattle.R
import com.example.seabattle.domain.errors.AuthError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.PresenceError
import com.example.seabattle.domain.errors.UserError


@StringRes
fun Throwable.toErrorMessageUI(): Int {
    return when (this) {
        is AuthError.InvalidCredentials -> R.string.error_invalid_credentials
        is AuthError.InvalidUser -> R.string.error_invalid_user
        is AuthError.UserCollision -> R.string.error_existing_user
        is AuthError.NetworkConnection -> R.string.error_network_connection
        is AuthError.Unknown -> R.string.error_unknown
        is DomainError.Unknown -> R.string.error_unknown
        is GameError.GameNotFound -> R.string.error_game_not_found
        is GameError.GameNotValid -> R.string.error_game_not_valid
        is GameError.InvalidGameState -> R.string.error_invalid_game_state
        is GameError.NetworkConnection -> R.string.error_network_connection
        is GameError.PermissionDenied -> R.string.error_permission_denied
        is GameError.Unknown -> R.string.error_unknown
        is PresenceError.UserNotAuthenticated -> R.string.error_user_not_auth
        is PresenceError.InvalidStatusValue -> R.string.error_presence_status
        is PresenceError.NetworkConnection -> R.string.error_network_connection
        is PresenceError.Unknown -> R.string.error_unknown
        is UserError.UserProfileNotFound -> R.string.error_user_not_found
        is UserError.NetworkConnection -> R.string.error_network_connection
        is UserError.PermissionDenied -> R.string.error_permission_denied
        is UserError.Unknown -> R.string.error_unknown
        else -> R.string.error_unknown
    }
}