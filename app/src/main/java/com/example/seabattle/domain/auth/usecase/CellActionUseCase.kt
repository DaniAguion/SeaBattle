package com.example.seabattle.domain.auth.usecase

class CellActionUseCase(

) {
    operator fun invoke(x: Int, y: Int) {
        println("Cell clicked at coordinates: ($x, $y)")
    }
}