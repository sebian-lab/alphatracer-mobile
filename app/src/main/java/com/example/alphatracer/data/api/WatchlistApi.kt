package com.example.alphatracer.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.alphatracer.data.model.response.WatchlistItem

/**
 * API interface for watchlist operations.
 */
interface WatchlistApi {
    
    /**
     * Get user's current watchlist.
     */
    @GET("/api/v1/watchlist")
    suspend fun getWatchlist(
        @Header(Constants.AUTHORIZATION_HEADER) token: String
    ): Response<List<WatchlistItem>>
    
    /**
     * Add a ticker to watchlist.
     */
    @POST("/api/v1/watchlist")
    suspend fun addToWatchlist(
        @Header(Constants.AUTHORIZATION_HEADER) token: String,
        @Body ticker: String
    ): Response<WatchlistItem>
    
    /**
     * Remove a ticker from watchlist.
     */
    @DELETE("/api/v1/watchlist/{ticker}")
    suspend fun removeFromWatchlist(
        @Header(Constants.AUTHORIZATION_HEADER) token: String,
        @Path("ticker") ticker: String
    ): Response<Unit>
}
