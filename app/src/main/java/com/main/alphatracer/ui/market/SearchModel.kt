package com.main.alphatracer.ui.market

data class StockSearchResult(
    val ticker: String,
    val name: String,
    val exchange: String? = null,
    val score: Int? = null
)