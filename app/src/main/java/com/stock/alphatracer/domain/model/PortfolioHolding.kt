package com.stock.alphatracer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

/**
 * Domain model for Portfolio Holding - links portfolio to stock holdings
 */
@Entity(tableName = "holdings")
data class PortfolioHolding(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var ticker: String,
    var shares: Double = 0.0,
    var avgCost: Double = 0.0,
    var currentValue: Double = 0.0,
    var totalCost: Double = 0.0,
    @Relation(
        parentColumn = "id",
        entity = Transaction::class
    )
    val transactions: List<Transaction> = emptyList()
)
