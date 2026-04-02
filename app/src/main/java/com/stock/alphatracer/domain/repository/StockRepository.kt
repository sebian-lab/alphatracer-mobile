package com.stock.alphatracer.domain.repository

import Result
import com.stock.alphatracer.domain.model.Stock

/**
 * Repository interface for stock operations
 */
interface StockRepository {
    
    /**
     * Search stocks by query (supports fuzzy matching)
     */
    suspend fun searchStocks(query: String): Result<Set<Stock>>
    
    /**
     * Get detailed stock information
     */
    suspend fun getStockDetails(ticker: String): Result<Stock>
}

