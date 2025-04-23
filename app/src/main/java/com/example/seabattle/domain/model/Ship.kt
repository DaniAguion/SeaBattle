package com.example.seabattle.domain.model

data class Ship(
    val size: Int,
    val position: Position = Position(0, 0, ShipDirection.VERTICAL),
)

data class Position (
    val x: Int,
    val y: Int,
    val shipDirection: ShipDirection
)

// The direction is done taking the top of the boat as static point.
enum class ShipDirection {
    VERTICAL,
    HORIZONTAL
}
