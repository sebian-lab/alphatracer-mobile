package com.stock.alphatracer.utils

/**
 * Constants for AlphaTracer application
 */
object Constants {
    // Base URL - Update to your backend endpoint
    const val BASE_URL = "https://your-backend.com/api/v1/"
    
    // Database names
    const val DATABASE_NAME = "alphatracer_db"
    
    // DataStore keys for authentication
    const val PREFS_NAME = "auth_prefs"
    const val ACCESS_TOKEN_KEY = "access_token"
    const val REFRESH_TOKEN_KEY = "refresh_token"
    const val USER_ID_KEY = "user_id"
    
    // Room Entity table names
    const val PORTFOLIO_TABLE = "portfolio"
    const val WATCHLIST_TABLE = "watchlist"
    const val TRANSACTION_TABLE = "transaction"
}
