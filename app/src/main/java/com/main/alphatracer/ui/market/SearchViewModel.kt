package com.main.alphatracer.ui.market


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.alphatracer.data.StockRepository

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<StockSearchResult> = emptyList(),
    val error: String? = null
)

class SearchViewModel(
    private val repository: StockRepository = StockRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun updateQuery(query: String) {
        _uiState.update { it.copy(query = query) }
        // Cancel ongoing search if any
        searchJob?.cancel()
        if (query.length >= 2) {
            searchJob = viewModelScope.launch {
                delay(300) // debounce
                performSearch(query)
            }
        } else {
            _uiState.update { it.copy(results = emptyList(), isLoading = false) }
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            val results = repository.searchStocks(query)
            // Only update if the query hasn't changed during the request
            if (_uiState.value.query == query) {
                _uiState.update { it.copy(results = results, isLoading = false) }
            }
        } catch (e: Exception) {
            if (_uiState.value.query == query) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Search failed")
                }
            }
        }
    }
}