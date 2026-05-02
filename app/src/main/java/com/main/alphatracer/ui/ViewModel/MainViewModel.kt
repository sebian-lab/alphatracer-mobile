// com.stock.alphatracer.ui.viewmodel.MainViewModel
package com.stock.alphatracer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.alphatracer.ui.Auth.Modulair.TokenManager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class MainViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn


    fun login() {
        _isLoggedIn.value = true
    }


}
