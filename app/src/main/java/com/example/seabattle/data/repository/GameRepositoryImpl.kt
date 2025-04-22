package com.example.seabattle.data.repository

import com.example.seabattle.domain.firestore.repository.FirestoreRepository
import com.example.seabattle.domain.game.repository.GameRepository

class GameRepositoryImpl(
    private val firestore: FirestoreRepository
) : GameRepository {
    override fun createGame() {
        TODO("Not yet implemented")
    }

    override fun cellAction(x: Int, y: Int) {
        println("Cell clicked at coordinates: ($x, $y)")
    }
}