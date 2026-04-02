package com.stock.alphatracer.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
 * Data class for Portfolio Card showing total value and daily change
 */
data class PortfolioCardData(
    val title: String,
    val value: String,
    val dailyChangePercent: String,
    val isPositive: Boolean = true
)

/**
 * Data class for Watchlist item
 */
data class WatchlistItem(
    val ticker: String,
    val name: String,
    val price: Double,
    val changePercent: String,
    val isPositive: Boolean = true
)

/**
 * Main home screen showing portfolio and watchlist
 */
@Composable
fun HomeScreen(
    onNavigateToStockDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    
    val portfolioCards = listOf(
        PortfolioCardData("Portfolio Value", "$12,345.67", "+2.34%", true),
        PortfolioCardData("Today's Gain", "+$289.45", "+2.34%", true)
    )
    
    val watchlistItems = listOf(
        WatchlistItem("AAPL", "Apple Inc.", 178.72, "+0.45%", true),
        WatchlistItem("MSFT", "Microsoft Corp.", 378.91, "+1.23%", true),
        WatchlistItem("GOOGL", "Alphabet Inc.", 151.52, "-0.67%", false)
    )
    
    AlphaTracerTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar with Greeting
                TopBar(text = "Good Morning, Trader!")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Portfolio Card
                if (isLoading) {
                    LoadingIndicator()
                } else {
                    error?.let { msg ->
                        Text(
                            text = "Error: ${msg}",
                            color = Color.Red,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        portfolioCards.forEachIndexed { index, card ->
                            item {
                                PortfolioCard(card) {
                                    onNavigateToStockDetail("${card.title}")
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Watchlist
                Text(
                    text = "Watchlist",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    watchlistItems.forEachIndexed { index, item ->
                        item {
                            WatchlistCard(item) {
                                onNavigateToStockDetail(item.ticker)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Portfolio Card Composable
 */
@Composable
fun PortfolioCard(
    data: PortfolioCardData,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier.size(160.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = data.title, fontSize = 12.sp, color = Color.Gray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = data.value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = data.dailyChangePercent,
                    color = if (data.isPositive) AlphaTracerTheme.colors.success else AlphaTracerTheme.colors.error,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

/**
 * Watchlist Card Composable
 */
@Composable
fun WatchlistCard(
    item: WatchlistItem,
    onClick: () -> Unit
) {
    FilledButton(
        onClick = onClick,
        modifier = Modifier.size(140.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = item.ticker, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${item.name}",
                    maxLines = 1,
                    overflow = android.text.TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.price.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = item.changePercent,
                    color = if (item.isPositive) AlphaTracerTheme.colors.success else AlphaTracerTheme.colors.error,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
