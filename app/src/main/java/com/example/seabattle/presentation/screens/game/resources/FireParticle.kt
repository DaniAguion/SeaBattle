package com.example.seabattle.presentation.screens.game.resources

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.random.Random

data class FireParticle(
    var x: Float,
    var y: Float,
    var radius: Float,
    var alpha: Float,
    val speedY: Float,
    val initialRadius: Float,
    val targetRadius: Float,
    val initialAlpha: Float,
    val targetAlpha: Float,
    val color: Color
) {
    fun update(deltaTime: Long) {
        y += speedY * (deltaTime / 1000f)

        val progress = (initialAlpha - alpha) / (initialAlpha - targetAlpha)
        radius = initialRadius + (targetRadius - initialRadius) * progress
        alpha -= (initialAlpha - targetAlpha) * (deltaTime / 1000f) * 0.5f
    }

    fun isDead(): Boolean {
        return alpha <= targetAlpha || radius >= targetRadius
    }

    companion object {
        fun createRandom(
            centerX: Float,
            startY: Float,
            maxRadius: Float,
            minSpeed: Float,
            maxSpeed: Float
        ): FireParticle {
            val radius = Random.nextFloat() * (maxRadius * 0.5f) + (maxRadius * 0.1f)
            val speed = -(Random.nextFloat() * (maxSpeed - minSpeed) + minSpeed)
            val alpha = Random.nextFloat() * (1.0f - 0.7f) + 0.7f
            val color = when (Random.nextInt(3)) {
                0 -> Color(0xFFFF4500)
                1 -> Color(0xFFFFA500)
                else -> Color(0xFFFF6347)
            }.copy(alpha = alpha)

            return FireParticle(
                x = centerX + Random.nextFloat() * (maxRadius * 0.8f) - (maxRadius * 0.4f),
                y = startY,
                radius = radius,
                alpha = alpha,
                speedY = speed,
                initialRadius = radius,
                targetRadius = maxRadius * 1.5f,
                initialAlpha = alpha,
                targetAlpha = 0f,
                color = color
            )
        }
    }
}


fun DrawScope.drawFireParticles(
    particles: List<FireParticle>,
    sourceCenter: Offset
) {
    particles.forEach { particle ->
        drawCircle(
            color = particle.color.copy(alpha = particle.alpha),
            radius = particle.radius,
            center = Offset(sourceCenter.x + particle.x, sourceCenter.y + particle.y)
        )
    }
}