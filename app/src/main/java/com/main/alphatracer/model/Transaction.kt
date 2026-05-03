package com.main.alphatracer.model

data class TransactionRequest(
    val stock_ticker: String,
    val type: String, // "buy"
    val quantity: Int,
    val price_per_share: Double,
    val transaction_date: String? = null // yyyy-MM-dd
)

data class TransactionResponse(
    val id: Int,
    val stock_ticker: String,
    val stock_name: String,
    val type: String,
    val quantity: Int,
    val price_per_share: Double,
    val transaction_date: String
)
