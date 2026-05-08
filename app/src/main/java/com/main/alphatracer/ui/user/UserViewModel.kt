package com.main.alphatracer.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.main.alphatracer.Auth.Modulair.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val user: User) : UserUiState()
    data class Error(val message: String) : UserUiState()
}
class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenManager = TokenManager.getInstance(application)

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState

    fun loadUser() {
        val name = tokenManager.getUserName()
        val email = tokenManager.getUserEmail()


        // Pass the 'id' here
        _uiState.value = UserUiState.Success(User( name = name, email = email))
    }
}