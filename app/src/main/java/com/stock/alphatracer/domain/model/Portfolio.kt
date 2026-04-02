package com.stock.alphatracer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Domain model for Portfolio - aggregates holdings and total value
 */
@Entity(tableName = "portfolios")
data class Portfolio(
    @PrimaryKey val id: String,
    var ticker: String,
    var shares: Double,
    var avgBuyPrice: Double,
    var currentValue: Double = 0.0,
    var totalCost: Double = 0.0,
    var pnl: Double = 0.0,
    var percentChange: Double = 0.0,
    val lastUpdated: Long = System.currentTimeMillis()
)
