package com.stock.alphatracer.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.fonts.FontWeight
import androidx.compose.ui.unit.sp
import com.stock.alphatracer.presentation.theme.AlphaTracerTheme

/**
 * Reusable top bar with back navigation and title
 */
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
    showBackButton: Boolean = true
) {
    TopAppBar(
        modifier = modifier,
        title = { if (title != null) Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AlphaTracerTheme.colors.primary,
            scrolledContainerColor = AlphaTracerTheme.colors.primary
        ),
        actions = actions
    )
}
