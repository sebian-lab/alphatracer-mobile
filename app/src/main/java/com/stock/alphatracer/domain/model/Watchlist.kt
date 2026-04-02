package com.stock.alphatracer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Domain model for Watchlist item
 */
@Entity(tableName = "watchlist")
data class WatchlistItem(
    @PrimaryKey val id: String,
    var ticker: String,
    var name: String? = null,
    var price: Double = 0.0,
    var change: Double = 0.0,
    var lastUpdated: Long = System.currentTimeMillis()
)
