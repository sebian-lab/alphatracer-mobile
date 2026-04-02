package com.stock.alphatracer.domain.repository

import Result

/**
 * Repository interface for watchlist operations
 */
interface WatchlistRepository {
    suspend fun getWatchlist(): Result<Set<Watchlist>>
    suspend fun addToWatchlist(watchlistItem: Watchlist): Result<Unit>
    suspend fun removeFromWatchlist(ticker: String): Result<Unit>
}
