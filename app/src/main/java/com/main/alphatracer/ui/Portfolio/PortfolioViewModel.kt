package com.main.alphatracer.ui.Portfolio


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.alphatracer.data.PortfolioRepository
import com.main.alphatracer.model.Holding
import com.main.alphatracer.network.RetrofitClient
import com.main.alphatracer.ui.Auth.Modulair.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PortfolioUiState {
    object Loading : PortfolioUiState()
    data class Success(val holdings: List<Holding>, val totalValue: Double) : PortfolioUiState()
    data class Error(val message: String) : PortfolioUiState()
}

class PortfolioViewModel : ViewModel() {

    private val tokenManager = TokenManager.getInstance()
    private val repository = PortfolioRepository(RetrofitClient.apiService)

    private val _uiState = MutableStateFlow<PortfolioUiState>(PortfolioUiState.Loading)
    val uiState: StateFlow<PortfolioUiState> = _uiState

    fun loadPortfolio() {
        viewModelScope.launch {
            _uiState.value = PortfolioUiState.Loading
            try {
                val token = tokenManager.getToken() ?: throw Exception("Not logged in")
                val holdings = repository.getUserHoldings(token)
                val totalValue = holdings.sumOf { it.quantity * it.currentPrice }
                _uiState.value = PortfolioUiState.Success(holdings, totalValue)
            } catch (e: Exception) {
                _uiState.value = PortfolioUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}