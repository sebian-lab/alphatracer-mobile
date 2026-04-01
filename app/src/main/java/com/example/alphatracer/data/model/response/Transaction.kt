package com.example.alphatracer.data.model.response

/**
 * Stock transaction (buy/sell).
 */
data class Transaction(
    val id: String?,
    val ticker: String?,
    val quantity: Double?,
    val price: Double?,
    val type: String? = null,
    val date: java.time.Instant? = null
)
