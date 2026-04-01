package com.example.alphatracer.data.model.response
import java.time.Instant

/**
 * Watchlist item containing stock information.
 */
data class WatchlistItem(
    val id: String?,
    val ticker: String,
    val name: String?,
    val current_price: Double?,
    val added_at: Instant? = null
)
