// com.stock.alphatracer.ui.viewmodel.MainViewModel
package com.stock.alphatracer.ui.viewmodel


import androidx.lifecycle.ViewModel
import com.main.alphatracer.ui.Auth.Modulair.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class Screen {
    Portfolio, Profile, Market
}
class MainViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _currentScreen = MutableStateFlow(Screen.Portfolio)
    val currentScreen: StateFlow<Screen> = _currentScreen

    private val _selectedTicker = MutableStateFlow<String?>(null)
    val selectedTicker: StateFlow<String?> = _selectedTicker

    private val _showAddTransaction = MutableStateFlow(false)
    val showAddTransaction: StateFlow<Boolean> = _showAddTransaction


    fun login() {
        _isLoggedIn.value = true
    }
    fun logout() {
        _isLoggedIn.value = false
        TokenManager.getInstance().clear()
    }

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setSelectedTicker(ticker: String?) {
        _selectedTicker.value = ticker
    }

    fun setShowAddTransaction(show: Boolean) {
        _showAddTransaction.value = show
    }

    fun clearSelectedTicker() {
        _selectedTicker.value = null
    }

}
