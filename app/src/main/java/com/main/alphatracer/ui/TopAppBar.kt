package com.stock.alphatracer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme

import com.stock.alphatracer.ui.screens.*
import com.main.alphatracer.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlphaTracerAppBar() {

    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .graphicsLayer { rotationZ = -8f }
                        .background(
                            brush = Brush.sweepGradient(
                                colors = listOf(primaryColor, tertiaryColor, primaryColor)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(2.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(surfaceColor)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.stylized),
                        contentDescription = "Logo",
                        modifier = Modifier.size(68.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "ALPHATRACER",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = onSurfaceColor
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = surfaceColor.copy(alpha = 0.95f)
        ),
        modifier = Modifier.drawBehind {
            val strokeWidth = 1.dp.toPx()

            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, primaryColor, Color.Transparent)
                ),
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = strokeWidth
            )
        }
    )
}