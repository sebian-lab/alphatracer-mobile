package com.example.alphatracer.data.model.response

/**
 * Portfolio containing total value and holdings.
 */
data class Portfolio(
    val total_value: Double?,
    val total_gain_loss: Double?,
    val holdings: List<Holding>? = null
)
