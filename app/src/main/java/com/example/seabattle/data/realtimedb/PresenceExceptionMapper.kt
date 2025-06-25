package com.example.seabattle.data.realtimedb

import com.example.seabattle.domain.errors.PresenceError
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException


fun Throwable.toPresenceError(): PresenceError {
    if (this is PresenceError) {
        return this
    }

    return when (this) {
        is DatabaseException -> {
            val message = this.message?.lowercase()
            when {
                message?.contains("unavailable") == true || message?.contains("disconnected") == true -> PresenceError.NetworkConnection(this)
                else -> PresenceError.Unknown(this)
            }
        }

        is java.net.UnknownHostException, is java.net.SocketTimeoutException, is java.io.IOException -> {
            PresenceError.NetworkConnection(this)
        }
        else -> {
            PresenceError.Unknown(this)
        }
    }
}


fun DatabaseError.toPresenceError(): PresenceError {
    return when (this.code) {
        DatabaseError.DISCONNECTED, DatabaseError.NETWORK_ERROR, DatabaseError.UNAVAILABLE -> PresenceError.NetworkConnection(this.toException())
        else -> PresenceError.Unknown(this.toException())
    }
}