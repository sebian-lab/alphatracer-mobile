package com.stock.alphatracer.data.model.request

import com.google.gson.annotations.SerializedName

/**
 * Request body for adding a transaction (buy/sell)
 */
data class TransactionRequest(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("type") val type: String, // "BUY" or "SELL"
    @SerializedName("date") val date: Long
)
