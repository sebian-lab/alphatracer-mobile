package com.stock.alphatracer.presentation.navigation

/**
 * Sealed class containing all navigation destinations/routes
 */
sealed class Destinations {
    object Login : Destinations()
    object Register : Destinations()
    object Home : Destinations()
    object Search : Destinations()
    data class StockDetail(val ticker: String) : Destinations()
    object Portfolio : Destinations()
    object Watchlist : Destinations()
    object Settings : Destinations()
}
