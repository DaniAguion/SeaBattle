package com.example.seabattle.domain.entity

data class Ship(
    val size: Int = 0,
    val shipBody: MutableList<ShipPiece> = MutableList(size) { ShipPiece() },
    val sunk: Boolean = false
)

data class ShipPiece(
    val x: Int = 0,
    val y: Int = 0,
    val touched: Boolean = false
)
