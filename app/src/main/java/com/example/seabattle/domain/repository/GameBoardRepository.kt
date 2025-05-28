package com.example.seabattle.domain.repository

interface GameBoardRepository {
    fun createGameBoard(): Result<Map<String, Map<String, Int>>>
}