package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.Ship

interface GameBoardRepository {
    fun createGameBoard(): Result<Unit>
    fun getGameBoard(): MutableMap<String, MutableMap<String, Int>>
    fun getShipList(): MutableList<Ship>
}