// com.stock.alphatracer.ui.viewmodel/StockDetailViewModel.kt
package com.main.alphatracer.ui.StockUi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.alphatracer.Auth.Modulair.TokenManager
import com.main.alphatracer.data.PortfolioRepository
import com.main.alphatracer.data.StockRepository
import com.main.alphatracer.model.MarketAnalysisResponse
import com.main.alphatracer.model.MetricsResponse
import com.main.alphatracer.model.TransactionRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class StockDetailUiState {
    object Loading : StockDetailUiState()
    data class Success(
        val metrics: MetricsResponse,
        val analysis: MarketAnalysisResponse
    ) : StockDetailUiState()

    data class Error(val message: String) : StockDetailUiState()
}

class StockDetailViewModel(
    private val stockRepository: StockRepository = StockRepository(),
    private val portfolioRepository: PortfolioRepository = PortfolioRepository(),
    private val tokenManager: TokenManager = TokenManager.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow<StockDetailUiState>(StockDetailUiState.Loading)
    val uiState: StateFlow<StockDetailUiState> = _uiState

    fun loadStockDetail(ticker: String) {
        viewModelScope.launch {
            _uiState.value = StockDetailUiState.Loading
            try {
                val (metrics, analysis) = stockRepository.fetchStockDetailRaw(ticker)
                _uiState.value = StockDetailUiState.Success(metrics, analysis)
            } catch (e: IOException) {
                _uiState.value = StockDetailUiState.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                _uiState.value = StockDetailUiState.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = StockDetailUiState.Error("Unexpected error: ${e.message}")
            }
        }
    }
    fun buyStock(ticker: String, quantity: Int, price: Double, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken() ?: throw Exception("Not logged in")
                val request = TransactionRequest(ticker, "buy", quantity, price)
                portfolioRepository.addTransaction(token, request)
                onSuccess()
            } catch (e: Exception) {
                // Handle error (e.g., show toast)
            }
        }
    }

    fun sellStock(ticker: String, quantity: Int, price: Double, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken() ?: throw Exception("Not logged in")
                val request = TransactionRequest(ticker, "sell", quantity, price)
                portfolioRepository.addTransaction(token, request)
                onSuccess()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    fun addToPortfolio(ticker: String, quantity: Int, price: Double) {
        viewModelScope.launch {
            try {

                val token = tokenManager.getToken() ?: throw Exception("Not logged in")
                val request = TransactionRequest(ticker, "buy", quantity, price)
                portfolioRepository.addTransaction(token, request)

            } catch (e: Exception) {

            }
        }
    }
}