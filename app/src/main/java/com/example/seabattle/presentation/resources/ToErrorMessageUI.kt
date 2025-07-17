package com.example.seabattle.presentation.resources

import androidx.annotation.StringRes
import com.example.seabattle.R
import com.example.seabattle.domain.errors.AuthError
import com.example.seabattle.domain.errors.DataError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.PresenceError
import com.example.seabattle.domain.errors.UserError


@StringRes
fun Throwable.toErrorMessageUI(): Int {
    return when (this) {
        is DomainError.Unknown -> R.string.error_unknown
        is DataError.NotFound -> R.string.error_data_not_found
        is DataError.Unknown -> R.string.error_unknown
        is DataError.NetworkConnection -> R.string.error_network_connection
        is DataError.PermissionDenied -> R.string.error_permission_denied
        is AuthError.InvalidCredentials -> R.string.error_invalid_credentials
        is AuthError.InvalidUser -> R.string.error_invalid_user
        is AuthError.UserCollision -> R.string.error_existing_user
        is AuthError.RecentLoginRequired -> R.string.error_recent_login_required
        is AuthError.NetworkConnection -> R.string.error_network_connection
        is AuthError.Unknown -> R.string.error_unknown
        is UserError.UserProfileNotFound -> R.string.error_user_not_found
        is UserError.UserGamesNotFound -> R.string.error_user_not_found
        is UserError.InvalidUserGamesData -> R.string.error_user_data_not_valid
        is UserError.InvalidGuest -> R.string.error_invalid_guest
        is GameError.GameNotFound -> R.string.error_game_not_found
        is GameError.InvalidData -> R.string.error_game_not_valid
        is GameError.InvalidGameState -> R.string.error_invalid_game_state
        is PresenceError.UserNotAuthenticated -> R.string.error_user_not_auth
        is PresenceError.InvalidStatusValue -> R.string.error_presence_status
        is PresenceError.NetworkConnection -> R.string.error_network_connection
        is PresenceError.Unknown -> R.string.error_unknown
        else -> R.string.error_unknown
    }
}