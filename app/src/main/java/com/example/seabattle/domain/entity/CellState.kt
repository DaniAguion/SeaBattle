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

    fun toBasic(): BasicCellState {
        when(this) {
            is HIDDEN_WATER, WATER -> return BasicCellState.WATER
            is SHIP, SHIP_TOP, SHIP_BOTTOM, SHIP_START, SHIP_END -> return BasicCellState.SHIP
            is HIT, HIT_TOP, HIT_BOTTOM, HIT_START, HIT_END -> return BasicCellState.HIT
            is SUNK, SUNK_TOP, SUNK_BOTTOM, SUNK_START, SUNK_END -> return BasicCellState.SUNK
        }
    }

    companion object {
        fun getFromValue(value: Int): CellState {
            return when (value) {
                0 -> HIDDEN_WATER
                1 -> SHIP
                2 -> SHIP_TOP
                3 -> SHIP_BOTTOM
                4 -> SHIP_START
                5 -> SHIP_END
                10 -> WATER
                11 -> HIT
                12 -> HIT_TOP
                13 -> HIT_BOTTOM
                14 -> HIT_START
                15 -> HIT_END
                21 -> SUNK
                22 -> SUNK_TOP
                23 -> SUNK_BOTTOM
                24 -> SUNK_START
                25 -> SUNK_END
                else -> throw IllegalArgumentException("Unknown cell state value: $value")
            }
        }
    }
}

enum class BasicCellState {
    SHIP,
    WATER,
    HIT,
    SUNK;
}



