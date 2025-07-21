package com.example.seabattle.presentation

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.seabattle.R
import androidx.core.content.edit

class SoundManager(private val context: Context) {
    private val soundPool: SoundPool
    private val soundMap = mutableMapOf<SoundEffect, Int>()
    private var loaded = false
    // Save the mute state in SharedPreferences
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("sound_prefs", Context.MODE_PRIVATE)
    private var _isMuted: Boolean = sharedPreferences.getBoolean("is_muted", false) // Leer el estado inicial

    val isMuted: Boolean
        get() = _isMuted


    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool.setOnLoadCompleteListener { _, _, _ ->
            loaded = true
        }

        loadSounds()
    }

    private fun loadSounds() {
        soundMap[SoundEffect.WATER_SPLASH] = soundPool.load(context, R.raw.water, 1)
        soundMap[SoundEffect.SHIP_HIT] = soundPool.load(context, R.raw.ship_hit, 1)
    }



    fun muteSound() {
        _isMuted = true
        sharedPreferences.edit { putBoolean("is_muted", true) }
    }

    fun unmuteSound() {
        _isMuted = false
        sharedPreferences.edit { putBoolean("is_muted", false) }
    }

    fun toggleMute() {
        if (_isMuted) {
            unmuteSound()
        } else {
            muteSound()
        }
    }


    fun checkIsMuted(): Boolean {
        return sharedPreferences.getBoolean("is_muted", false)
    }


    private fun playSound(effect: SoundEffect) {
        val soundMuted = checkIsMuted()

        if (!soundMuted && loaded && soundMap.containsKey(effect)) {
            soundPool.play(
                soundMap[effect]!!,
                1f,
                1f,
                1,
                0,
                1f
            )
        }
    }

    fun playWaterSplash() {
        playSound(SoundEffect.WATER_SPLASH)
    }

    fun playShipHit() {
        playSound(SoundEffect.SHIP_HIT)
    }


    // Release the sound pool resources when no longer needed
    fun release() {
        soundPool.release()
    }


    enum class SoundEffect {
        WATER_SPLASH,
        SHIP_HIT
    }
}