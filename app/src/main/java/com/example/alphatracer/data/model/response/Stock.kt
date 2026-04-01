package com.example.alphatracer.data.model.response

/**
 * Stock information.
 */
data class Stock(
    val ticker: String?,
    val name: String?,
    val sector: String?,
    val industry: String?,
    val current_price: Double?
)
