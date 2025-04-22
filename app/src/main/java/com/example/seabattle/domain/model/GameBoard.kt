package com.example.seabattle.domain.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlin.Int

data class GameBoard(
    var cells: SnapshotStateList<SnapshotStateList<Int>> = mutableStateListOf<SnapshotStateList<Int>>().apply {
        repeat(10) {
            add(mutableStateListOf<Int>().apply {
                repeat(10) { add(0) }
            })
        }
    }
)