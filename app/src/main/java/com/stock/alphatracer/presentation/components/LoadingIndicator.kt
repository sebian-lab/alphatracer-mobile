package com.stock.alphatracer.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.fonts.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stock.alphatracer.presentation.theme.AlphaTracerTheme

/**
 * Animated circular loading indicator
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    contentColor: Color = AlphaTracerTheme.colors.primary,
    size: Float = 48f
) {
    val spinTransition = rememberInfiniteTransition()
    
    Canvas(modifier = modifier.size(size.dp)) {
        repeat(3) { iteration ->
            val colorIndex = (iteration + contentColor.rgbToGray()) % 3
            val color = when (colorIndex) {
                0 -> AlphaTracerTheme.colors.primary.copy(alpha = 0.7f)
                1 -> AlphaTracerTheme.colors.primary.copy(alpha = 0.5f)
                else -> AlphaTracerTheme.colors.primary.copy(alpha = 0.3f)
            }
            
            val rotationOffset = spinTransition.animateFloat(
                initialValue = iteration * 60f,
                targetValue = { it },
                animationSpec = infiniteRepeatable(
                    tween(800, repeatMode = RepeatMode.Reverse),
                    tween(1000)
                ),
                label = "spin"
            )
            
            drawCircle(
                color = color,
                center = Offset(size / 2 + rotationOffset * (iteration - 1).toFloat() / 3f, size / 2),
                radius = size / 2
            )
        }
    }
}
