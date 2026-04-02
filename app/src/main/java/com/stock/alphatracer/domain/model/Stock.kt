package com.stock.alphatracer.domain.model

/**
 * Domain model for Stock - cleaned version without API-specific fields
 */
data class Stock(
    val ticker: String,
    val name: String,
    var price: Double,
    var change: Double = 0.0,
    var percentChange: Double = 0.0
)
