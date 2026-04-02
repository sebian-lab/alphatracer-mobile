package com.stock.alphatracer.presentation.ui.auth

import androidx.compose.runtime.Immutable

/**
 * Sealed class representing authentication UI states
 */
@Immutable
sealed class AuthUiState {
    object Loading : AuthUiState()
    data class Success(val email: String, val user: User?) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
