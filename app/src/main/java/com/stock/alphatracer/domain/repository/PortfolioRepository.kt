package com.stock.alphatracer.domain.repository

import Result
import com.stock.alphatracer.domain.model.PortfolioHolding
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for portfolio operations
 */
interface PortfolioRepository {
    
    /**
     * Get current user's portfolio holdings
     */
    suspend fun getPortfolio(): Result<Set<PortfolioHolding>>
    
    /**
     * Add a new transaction (buy/sell)
     */
    suspend fun addTransaction(ticker: String, quantity: Int, price: Double, type: String): Result<Unit>
    
    /**
     * Get real-time portfolio summary
     */
    fun getPortfolioSummary(): Flow<PortfolioSummary?>
}

/**
 * Data class for portfolio summary with aggregated data
 */
data class PortfolioSummary(
    val totalValue: Double,
    val totalCostBasis: Double,
    val totalGainLoss: Double,
    val gainLossPercentage: Float
)
