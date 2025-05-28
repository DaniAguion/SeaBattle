package com.example.seabattle.domain.entity

// Data class used to check ship position and direction during placement
data class ShipPosition (
    val x: Int = 0,
    val y: Int = 0,
    val shipDirection: ShipDirection
)

enum class ShipDirection {
    VERTICAL,
    HORIZONTAL
}