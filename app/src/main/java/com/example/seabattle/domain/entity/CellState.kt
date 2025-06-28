package com.example.seabattle.domain.entity

sealed class CellState(val value: Int) {
    object HIDDEN_WATER : CellState(
        value = 0
    )
    object SHIP : CellState(
        value = 1
    )
    object SHIP_TOP : CellState(
        value = 2
    )
    object SHIP_BOTTOM : CellState(
        value = 3
    )
    object SHIP_START : CellState(
        value = 4
    )
    object SHIP_END : CellState(
        value = 5
    )
    object WATER : CellState(
        value = 10
    )
    object HIT : CellState(
        value = 11
    )
    object HIT_TOP : CellState(
        value = 12
    )
    object HIT_BOTTOM : CellState(
        value = 13
    )
    object HIT_START : CellState(
        value = 14
    )
    object HIT_END : CellState(
        value = 15
    )
    object SUNK : CellState(
        value = 21
    )
    object SUNK_TOP : CellState(
        value = 22
    )
    object SUNK_BOTTOM : CellState(
        value = 23
    )
    object SUNK_START : CellState(
        value = 24
    )
    object SUNK_END : CellState(
        value = 25
    )
}


