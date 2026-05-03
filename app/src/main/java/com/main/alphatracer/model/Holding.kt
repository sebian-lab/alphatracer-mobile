package com.main.alphatracer.model

data class Holding(
    val ticker: String,
    val name: String,
    val quantity: Int,
    val currentPrice: Double
)