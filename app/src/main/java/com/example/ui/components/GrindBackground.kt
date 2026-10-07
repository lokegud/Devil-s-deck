package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DevilBlood
import com.example.ui.theme.SinBlack
import com.example.ui.theme.SinSurface

/**
 * Custom Sin City style dark atmosphere background with film grind,
 * comic halftone dot matrix and deep crimson vignette.
 */
@Composable
fun GrindBackground(
    modifier: Modifier = Modifier,
    showEmbers: Boolean = true,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SinBlack)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Deep crimson top glow (Devil's lair)
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        DevilBlood.copy(alpha = 0.35f),
                        SinSurface.copy(alpha = 0.2f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.5f, height * 0.12f),
                    radius = width * 0.85f
                )
            )

            // 2. Bottom deep noir vignette
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        SinBlack.copy(alpha = 0.85f),
                        SinBlack
                    ),
                    startY = height * 0.65f,
                    endY = height
                )
            )

            // 3. Comic grind / halftone dot matrix
            val step = 36f
            var y = 0f
            while (y < height) {
                var x = (y % (step * 2)) / 2f
                while (x < width) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.035f),
                        radius = 1.2f,
                        center = Offset(x, y)
                    )
                    x += step
                }
                y += step
            }

            // 4. Subtle red ember sparks if enabled
            if (showEmbers) {
                val emberOffsets = listOf(
                    Offset(width * 0.18f, height * 0.28f),
                    Offset(width * 0.82f, height * 0.22f),
                    Offset(width * 0.25f, height * 0.62f),
                    Offset(width * 0.74f, height * 0.75f),
                    Offset(width * 0.48f, height * 0.40f)
                )
                for (offset in emberOffsets) {
                    drawCircle(
                        color = Color(0xFFFF2A4D).copy(alpha = 0.3f),
                        radius = 2.5f,
                        center = offset
                    )
                }
            }
        }

        content()
    }
}
