package com.example.alphatracer.data.model.response

/**
 * Individual holding in a portfolio.
 */
data class Holding(
    val ticker: String?,
    val quantity: Double?,
    val average_price: Double?,
    val current_price: Double?,
    val total_value: Double?,
    val gain_loss: Double?
)
