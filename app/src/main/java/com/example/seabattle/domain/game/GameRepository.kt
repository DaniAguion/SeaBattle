package com.example.seabattle.domain.game

interface GameRepository {
    fun createGame()
    fun cellAction(x: Int, y: Int)
}