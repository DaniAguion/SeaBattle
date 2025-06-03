package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Ship

interface GameBoardRepository {
    fun createGameBoard(): Result<Unit>
    fun getGameBoard(): Map<String, Map<String, Int>>
    fun getShipList(): List<Ship>
}