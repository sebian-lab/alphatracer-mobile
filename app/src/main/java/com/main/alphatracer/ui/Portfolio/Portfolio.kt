package com.main.alphatracer.ui.Portfolio

import com.google.gson.annotations.SerializedName

data class PortfolioResponse(
    val user_id: Int,
    val total_cost: Double,
    val current_value: Double,
    val holdings: List<HoldingResponse>,
    @SerializedName("gain_loss_pct") val gain_loss_pct: Double
)
data class Holding(
    val ticker: String,
    val name: String,
    val quantity: Int,
    val currentPrice: Double,
    val gainLossPct: Double
)
data class HoldingResponse(
    val stock_ticker: String,
    val stock_name: String,
    val quantity: Int,
    val average_price: Double,
    val current_price: Double,
    val gain_loss: Double
)