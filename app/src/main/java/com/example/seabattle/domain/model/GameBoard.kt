package com.example.seabattle.domain.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlin.Int

data class GameBoard(
    val cells: SnapshotStateList<SnapshotStateList<Int>> = mutableStateListOf<SnapshotStateList<Int>>().apply {
        repeat(10) {
            add(mutableStateListOf<Int>().apply {
                repeat(10) { add(0) }
            })
        }
    }
){

    fun toMapOfMaps(): Map<String, Map<String, Int>> {
        return cells.mapIndexed { rowIndex, row ->
            rowIndex.toString() to row.mapIndexed { colIndex, value ->
                colIndex.toString() to value
            }.toMap()
        }.toMap()
    }
}
