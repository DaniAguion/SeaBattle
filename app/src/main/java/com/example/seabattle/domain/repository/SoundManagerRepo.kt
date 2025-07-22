package com.example.seabattle.domain.repository

interface SoundManagerRepo {
    val isMuted: Boolean
    fun toggleMute()
    fun playWaterSplash()
    fun playShipHit()
    fun playVictory()
    fun playDefeat()
    fun release()
}