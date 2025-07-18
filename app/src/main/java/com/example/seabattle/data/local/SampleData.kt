package com.example.seabattle.data.local

import com.example.seabattle.domain.entity.BasicPlayer
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameHistory
import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.domain.entity.Player
import com.example.seabattle.domain.entity.Ship
import com.example.seabattle.domain.entity.ShipPiece
import com.example.seabattle.domain.entity.User


val sampleUser = User(
    userId = "dLvCWzXgbAhcTqYqiR5iFKYDGgS2",
    displayName = "MeuPixel",
    email = "testing@gmail.com",
    photoUrl = "",
    status = "online",
    score = 1500,
    history = listOf(
        GameHistory(
            gameId = "1",
            winnerId = "dLvCWzXgbAhcTqYqiR5iFKYDGgS2",
            player1 = BasicPlayer(userId = "dLvCWzXgbAhcTqYqiR5iFKYDGgS2", displayName = "MeuPixel", score = 1500),
            player2 = BasicPlayer(userId = "MFqfjTZM3lhKkNJRhGhMuV9T6MK2", displayName = "Daniel", score = 1400),
            scoreTransacted = 100
        ),
        GameHistory(
            gameId = "2",
            winnerId = "dLvCWzXgbAhcTqYqiR5iFKYDGgS2",
            player1 = BasicPlayer(userId = "dLvCWzXgbAhcTqYqiR5iFKYDGgS2", displayName = "MeuPixel", score = 1500),
            player2 = BasicPlayer(userId = "MFqfjTZM3lhKkNJRhGhMuV9T6MK2", displayName = "Daniel", score = 1400),
            scoreTransacted = 100
        )
    )

)


val sampleGame = Game(
    gameId="f074ffb3-2bdd-4edc-97b4-8a423d3705af",
    player1= Player(
        userId = "dLvCWzXgbAhcTqYqiR5iFKYDGgS2",
        displayName = "MeuPixel",
        photoUrl = ""
    ),
    boardForPlayer2 = mapOf(
        "0" to mapOf("0" to 4, "1" to 1, "2" to 1, "3" to 5, "4" to 0, "5" to 4, "6" to 1, "7" to 5, "8" to 0, "9" to 0),
        "1" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
        "2" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
        "3" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
        "4" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 4, "7" to 1, "8" to 5, "9" to 0),
        "5" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
        "6" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
        "7" to mapOf("0" to 4, "1" to 1, "2" to 1, "3" to 1, "4" to 5, "5" to 0, "6" to 2, "7" to 0, "8" to 0, "9" to 0),
        "8" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 3, "7" to 0, "8" to 0, "9" to 0),
        "9" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0)
    ),
    player1Ready=false,
    player1Ships = listOf(
        Ship(
            size = 5,
            shipBody = listOf(
                ShipPiece(x = 3, y = 6, touched = false),
                ShipPiece(x = 4, y = 6, touched = false),
                ShipPiece(x = 5, y = 6, touched = false),
                ShipPiece(x = 6, y = 6, touched = false),
                ShipPiece(x = 7, y = 6, touched = false)
            ),
            sunk = false
        ),
        Ship(
            size = 4,
            shipBody = listOf(
                ShipPiece(x = 0, y = 0, touched = false),
                ShipPiece(x = 0, y = 1, touched = false),
                ShipPiece(x = 0, y = 2, touched = false),
                ShipPiece(x = 0, y = 3, touched = false)
            ),
            sunk = false
        ),
        Ship(
            size = 3,
            shipBody = listOf(
                ShipPiece(x = 8, y = 0, touched = false),
                ShipPiece(x = 8, y = 1, touched = false),
                ShipPiece(x = 8, y = 2, touched = false)
            ),
            sunk = false
        ),
        Ship(
            size = 3,
            shipBody = listOf(
                ShipPiece(x = 5, y = 9, touched = false),
                ShipPiece(x = 6, y = 9, touched = false),
                ShipPiece(x = 7, y = 9, touched = false)
            ),
            sunk = false
        ),
        Ship(
            size = 2,
            shipBody = listOf(
                ShipPiece(x = 3, y = 1, touched = false),
                ShipPiece(x = 4, y = 1, touched = false)
            ),
            sunk = false
        )
    ),
    player2=Player(
        userId="MFqfjTZM3lhKkNJRhGhMuV9T6MK2",
        displayName="Daniel",
        photoUrl="https://lh3.googleusercontent.com/a/ACg8ocJNvKdeillcj8hhgyN3qMbyXCTUtC3Hcm5-p9HwRFHEos2tcsQ=s96-c"
    ),
    player2Ready = false,
    boardForPlayer1 = mapOf(
        "0" to mapOf("0" to 4, "1" to 1, "2" to 1, "3" to 5, "4" to 0, "5" to 4, "6" to 1, "7" to 5, "8" to 0, "9" to 0),
        "1" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
        "2" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
        "3" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
        "4" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 4, "7" to 1, "8" to 5, "9" to 0),
        "5" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
        "6" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
        "7" to mapOf("0" to 4, "1" to 1, "2" to 1, "3" to 1, "4" to 5, "5" to 0, "6" to 2, "7" to 0, "8" to 0, "9" to 0),
        "8" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 3, "7" to 0, "8" to 0, "9" to 0),
        "9" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0)
    ),
    player2Ships = listOf(
        Ship(
            size = 5,
            shipBody = listOf(
                ShipPiece(x = 1, y = 0, touched = false),
                ShipPiece(x = 1, y = 1, touched = false),
                ShipPiece(x = 1, y = 2, touched = false),
                ShipPiece(x = 1, y = 3, touched = false),
                ShipPiece(x = 1, y = 4, touched = false)
            ),
            sunk = false
        ),
        Ship(
            size = 4,
            shipBody = listOf(
                ShipPiece(x = 6, y = 2, touched = false),
                ShipPiece(x = 6, y = 3, touched = false),
                ShipPiece(x = 6, y = 4, touched = false),
                ShipPiece(x = 6, y = 5, touched = false)
            ),
            sunk = false
        ),
        Ship(
            size = 3,
            shipBody = listOf(
                ShipPiece(x = 0, y = 6, touched = false),
                ShipPiece(x = 0, y = 7, touched = false),
                ShipPiece(x = 0, y = 8, touched = false)
            ),
            sunk = false
        ),
        Ship(
            size = 3,
            shipBody = listOf(
                ShipPiece(x = 3, y = 0, touched = false),
                ShipPiece(x = 4, y = 0, touched = false),
                ShipPiece(x = 5, y = 0, touched = false)
            ),
            sunk = false
        ),
        Ship(
            size = 2,
            shipBody = listOf(
                ShipPiece(x = 5, y = 8, touched = false),
                ShipPiece(x = 6, y = 8, touched = false)
            ),
            sunk = false
        )
    ),
    currentPlayer="dLvCWzXgbAhcTqYqiR5iFKYDGgS2",
    gameState="GAME_IN_PROGRESS",
    winnerId=null
)

val sampleBasicPlayersList = listOf(
    BasicPlayer(userId = "1", displayName = "Player1", score = 100),
    BasicPlayer(userId = "2", displayName = "Daniel Alvarez Aguion", score = 90),
    BasicPlayer(userId = "3", displayName = "Player3", score = 80),
    BasicPlayer(userId = "4", displayName = "Player4", score = 70),
    BasicPlayer(userId = "5", displayName = "Player5", score = 60)
)


val sampleGameHistory = GameHistory(
    gameId = "1",
    winnerId = sampleGame.player1.userId,
    player1 = sampleBasicPlayersList[0],
    player2 = sampleBasicPlayersList[1],
    scoreTransacted = 14,
    playedAt = sampleGame.updatedAt
)


val sampleInvitation = Invitation(
    gameId = "f074ffb3-2bdd-4edc-97b4-8a423d3705af",
    invitedTo = sampleBasicPlayersList[0],
    invitedBy = sampleBasicPlayersList[1]
)