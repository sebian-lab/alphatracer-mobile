package com.stock.alphatracer.presentation.ui.stocks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.fonts.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stock.alphatracer.presentation.components.BuySellSheet
import com.stock.alphatracer.presentation.theme.AlphaTracerTheme

/**
 * Buy/Sell screen with modal bottom sheet for transaction details
 */
@Composable
fun BuySellScreen(
    type: String = "Buy", // "Buy" or "Sell"
    onConfirm: (quantity: Int, price: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }
    
    AlphaTracerTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    Text(
                        text = type,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (type == "Buy") AlphaTracerTheme.colors.success else AlphaTracerTheme.colors.error
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { isExpanded = !isExpanded },
                            modifier = Modifier.size(100.dp)
                        ) {
                            Text("${type} $quantity Shares", fontSize = 16.sp)
                        }
                        
                        FilledTonalButton(
                            onClick = onConfirm,
                            enabled = false
                        ) {
                            Text("${if (type == "Buy") "Add" else "Sell"}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
