package com.example.seabattle.data.sound

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.core.content.edit
import com.example.seabattle.R
import com.example.seabattle.domain.repository.SoundManagerRepo

class SoundManagerImpl(private val context: Context) : SoundManagerRepo {
    private val soundPool: SoundPool
    private val soundMap = mutableMapOf<SoundEffect, Int>()
    private var loaded = false
    // Save the mute state in SharedPreferences

    // SharedPreferences to persist the mute state across app restarts
    // Define the necessary isMuted variable
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("sound_prefs", Context.MODE_PRIVATE)
    private var _isMuted: Boolean = sharedPreferences.getBoolean("is_muted", false)
    override val isMuted: Boolean
        get() = _isMuted


    // Enum class to define sound effects
    enum class SoundEffect {
        WATER_SPLASH,
        SHIP_HIT
    }


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


    private fun playSound(effect: SoundEffect) {
        if (!isMuted && loaded && soundMap.containsKey(effect)) {
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

    override fun toggleMute() {
        _isMuted = !isMuted
        sharedPreferences.edit { putBoolean("is_muted", isMuted) }
    }

    override fun playWaterSplash() {
        playSound(SoundEffect.WATER_SPLASH)
    }

    override fun playShipHit() {
        playSound(SoundEffect.SHIP_HIT)
    }


    // Release the sound pool resources when no longer needed
    override fun release() {
        soundPool.release()
    }
}