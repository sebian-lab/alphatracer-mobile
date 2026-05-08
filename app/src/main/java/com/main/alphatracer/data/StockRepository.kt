package com.main.alphatracer.data

import com.main.alphatracer.model.MarketAnalysisResponse
import com.main.alphatracer.model.MetricsResponse
import com.main.alphatracer.network.RetrofitClient
import com.main.alphatracer.ui.market.StockSearchResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class StockRepository {
    private val api = RetrofitClient.apiService

    /** Fetch both metrics and market analysis in parallel */
    suspend fun fetchStockDetailRaw(ticker: String): Pair<MetricsResponse, MarketAnalysisResponse> = coroutineScope {
        val metricsDeferred = async { api.getStockMetrics(ticker) }
        val analysisDeferred = async { api.getMarketAnalysis(ticker) }
        metricsDeferred.await() to analysisDeferred.await()
    }


    suspend fun searchStocks(query: String, limit: Int = 20): List<StockSearchResult> {
        return api.searchStocks(query, limit)
    }
}