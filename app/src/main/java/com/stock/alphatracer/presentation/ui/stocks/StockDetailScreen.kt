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
import com.stock.alphatracer.presentation.theme.AlphaTracerTheme

/**
 * Data class for stock details
 */
data class StockDetails(
    val ticker: String,
    var name: String,
    var price: Double,
    val changePercent: String,
    val isPositive: Boolean = true
)

/**
 * Main stock detail screen
 */
@Composable
fun StockDetailScreen(
    onNavigateToBuySell: () -> Unit,
    modifier: Modifier = Modifier
) {
    var stock by remember { mutableStateOf(StockDetails("AAPL", "Apple Inc.", 178.72, "+0.45%", true)) }
    var isLoading by remember { mutableStateOf(true) }
    
    AlphaTracerTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar
                TopBar(title = stock.ticker)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Stock Header
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = stock.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${stock.ticker}",
                            color = AlphaTracerTheme.colors.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${stock.price}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stock.changePercent,
                                color = if (stock.isPositive) AlphaTracerTheme.colors.success else AlphaTracerTheme.colors.error,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Stock Stats (placeholders)
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(5) { index ->
                        StatCard("Market Cap", "$2.1T")
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Actions
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilledButton(onClick = onNavigateToBuySell) {
                        Text("Buy", fontSize = 16.sp)
                    }
                    OutlinedButton(
                        onClick = { /* Navigate to sell */ },
                        enabled = false
                    ) {
                        Text("Sell", fontSize = 16.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Watchlist Toggle
                OutlinedButton(
                    onClick = { /* Toggle watchlist */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add to Watchlist", fontSize = 16.sp)
                }
            }
        }
    }
}

/**
 * Stat Card for stock details
 */
@Composable
fun StatCard(title: String, value: String) {
    FilledTonalButton(
        onClick = { },
        modifier = Modifier.size(120.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = title, fontSize = 10.sp, color = Color.Gray)
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}
