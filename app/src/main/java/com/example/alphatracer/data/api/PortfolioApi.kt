package com.example.alphatracer.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import com.example.alphatracer.data.model.response.Holding
import com.example.alphatracer.data.model.response.Portfolio
import com.example.alphatracer.data.model.request.TransactionRequest
import com.example.alphatracer.data.model.response.Transaction

/**
 * API interface for portfolio operations.
 */
interface PortfolioApi {
    
    /**
     * Get user's current portfolio.
     */
    @GET("/api/v1/portfolio")
    suspend fun getPortfolio(
        @Header(Constants.AUTHORIZATION_HEADER) token: String
    ): Response<Portfolio>
    
    /**
     * Add a transaction to the portfolio.
     */
    @POST("/api/v1/portfolio/transactions")
    suspend fun addTransaction(
        @Header(Constants.AUTHORIZATION_HEADER) token: String,
        @Body request: TransactionRequest
    ): Response<Transaction>
}
