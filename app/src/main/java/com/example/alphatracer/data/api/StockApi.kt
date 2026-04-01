package com.example.alphatracer.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.alphatracer.data.model.response.Stock

/**
 * API interface for stock operations.
 */
interface StockApi {
    
    /**
     * Search for stocks by ticker, name, or sector.
     */
    @GET("/api/v1/stocks/search")
    suspend fun searchStocks(
        @Query(Constants.QUERY_PARAM) query: String
    ): Response<List<Stock>>
    
    /**
     * Get stock details by ticker symbol.
     */
    @GET("/api/v1/stocks/{ticker}")
    suspend fun getStockByTicker(
        @Path("ticker") ticker: String
    ): Response<Stock>
}
