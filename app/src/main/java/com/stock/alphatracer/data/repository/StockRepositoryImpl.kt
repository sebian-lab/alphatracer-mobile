package com.stock.alphatracer.data.repository

import com.stock.alphatracer.domain.repository.StockRepository
import Result

/**
 * Implementation of StockRepository interface
 */
class StockRepositoryImpl : StockRepository {
    private var stocksCache: MutableSet<Stock> = mutableSetOf()
    
    override suspend fun searchStocks(query: String): Result<Set<Stock>> {
        // TODO: Implement API call to /stocks endpoint with query parameter
        return Result.success(stocksCache)
    }
    
    override suspend fun getStockDetails(ticker: String): Result<Stock> {
        // TODO: Implement API call to /stocks/{ticker} endpoint
        return Result.failure(Exception("Get stock details not implemented yet"))
    }
}
