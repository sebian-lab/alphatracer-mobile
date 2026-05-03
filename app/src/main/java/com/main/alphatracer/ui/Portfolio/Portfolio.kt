package com.main.alphatracer.ui.Portfolio
data class PortfolioResponse(
    val user_id: Int,
    val total_cost: Double,
    val current_value: Double,
    val holdings: List<HoldingResponse>
)

data class HoldingResponse(
    val stock_ticker: String,
    val stock_name: String,
    val quantity: Int,
    val average_price: Double,
    val current_price: Double,
    val gain_loss: Double
)