package com.example.seabattle.domain.game.repository

interface GameRepository {
    fun createGame()
    fun cellAction(x: Int, y: Int)
}