package com.example.seabattle.data.firestore.mappers

import com.google.firebase.Timestamp
import java.time.Instant

fun Instant.toTimestamp(): Timestamp =
    Timestamp(this.epochSecond, this.nano)