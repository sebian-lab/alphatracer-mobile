// MainActivity.kt
package com.stock.alphatracer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.main.alphatracer.ui.Auth.Modulair.TokenManager


import com.stock.alphatracer.ui.screens.*
import com.stock.alphatracer.ui.viewmodel.MainViewModel



class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize TokenManager once
        TokenManager.getInstance(applicationContext)
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
