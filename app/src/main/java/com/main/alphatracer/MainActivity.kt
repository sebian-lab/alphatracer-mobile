// MainActivity.kt
package com.stock.alphatracer


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.main.alphatracer.ui.Alert.scheduleStockWorker
import com.main.alphatracer.ui.Auth.Modulair.TokenManager
import com.stock.alphatracer.ui.screens.AuthScreen
import com.stock.alphatracer.ui.viewmodel.MainViewModel


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tokenManager = TokenManager.getInstance(applicationContext)

        if (tokenManager.getToken() != null) {
            scheduleStockWorker(this)
        }
        setContent {
            AppTheme {
                val mainViewModel: MainViewModel = viewModel()
                val isLoggedIn by mainViewModel.isLoggedIn.collectAsState()


                if (!isLoggedIn) {
                    AuthScreen(
                        onLoginSuccess = { mainViewModel.login() },
                        tokenManager = TokenManager.getInstance()
                    )
                }
                }
            }
        }
    }
