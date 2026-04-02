package com.stock.alphatracer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Domain model for Transaction - buy/sell operations
 */
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey val id: String,
    var ticker: String,
    var quantity: Double,
    var price: Double,
    var type: String = "BUY", // BUY or SELL
    var timestamp: Long = System.currentTimeMillis()
)
