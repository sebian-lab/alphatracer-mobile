package com.stock.alphatracer.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Stock response model from API
 */
data class Stock(
    @SerializedName("id") val id: String,
    @SerializedName("ticker") val ticker: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("change") val change: Double,
    @SerializedName("percentChange") val percentChange: Double,
    @SerializedName("volume") val volume: Long = 0L,
    @SerializedName("marketCap") val marketCap: String? = null
)
