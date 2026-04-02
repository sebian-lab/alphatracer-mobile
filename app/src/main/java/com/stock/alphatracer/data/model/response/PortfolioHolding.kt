package com.stock.alphatracer.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Portfolio holding from API response
 */
data class PortfolioHolding(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("shares") val shares: Double,
    @SerializedName("avgBuyPrice") val avgBuyPrice: Double,
    @SerializedName("lastUpdated") val lastUpdated: Long = 0L
)
