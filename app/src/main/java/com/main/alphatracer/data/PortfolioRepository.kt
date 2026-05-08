package com.main.alphatracer.data


import com.main.alphatracer.model.TransactionRequest
import com.main.alphatracer.model.TransactionResponse
import com.main.alphatracer.network.ApiService
import com.main.alphatracer.network.RetrofitClient
import com.main.alphatracer.ui.Portfolio.Holding
import com.main.alphatracer.ui.Portfolio.PortfolioResponse

class PortfolioRepository(
    private val api: ApiService = RetrofitClient.apiService
) {
    suspend fun getUserHoldings(token: String): List<Holding> {
        val response: PortfolioResponse = api.getPortfolio("Bearer $token")
        return response.holdings.map {
            Holding(
                ticker = it.stock_ticker,
                name = it.stock_name,
                quantity = it.quantity,
                currentPrice = it.current_price
            )
        }
    }

    suspend fun addTransaction(token: String, request: TransactionRequest): TransactionResponse {
        return api.addTransaction("Bearer $token", request)
    }
}