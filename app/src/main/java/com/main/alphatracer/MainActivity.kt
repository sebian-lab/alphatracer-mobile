// MainActivity.kt
package com.stock.alphatracer


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.main.alphatracer.Auth.Modulair.TokenManager
import com.main.alphatracer.ui.Alert.scheduleStockWorker
import com.main.alphatracer.ui.Portfolio.PortfolioUi
import com.main.alphatracer.ui.StockUi.StockDetailScreen
import com.main.alphatracer.ui.market.MarketScreen
import com.main.alphatracer.ui.user.UserUi
import com.stock.alphatracer.ui.AlphaTracerAppBar
import com.stock.alphatracer.ui.screens.AuthScreen
import com.stock.alphatracer.ui.viewmodel.MainViewModel
import com.stock.alphatracer.ui.viewmodel.Screen


class MainActivity : FragmentActivity() {
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
                val currentScreen by mainViewModel.currentScreen.collectAsState()
                val selectedTicker by mainViewModel.selectedTicker.collectAsState()


                if (!isLoggedIn) {

                    AuthScreen(
                        onLoginSuccess = { mainViewModel.login() },
                        tokenManager = TokenManager.getInstance()
                    )
                }else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            AlphaTracerAppBar()
                        },
                        bottomBar = {
                            NavigationBar(tonalElevation = 8.dp) {
                                Screen.values().forEach { screen ->
                                    NavigationBarItem(
                                        selected = currentScreen == screen,
                                        onClick = {
                                                mainViewModel.clearSelectedTicker()
                                                mainViewModel.setCurrentScreen(screen)
                                                  },

                                        label = { Text(screen.name) },
                                        icon = {
                                            Icon(
                                                imageVector = when (screen) {
                                                    Screen.Market -> Icons.AutoMirrored.Filled.TrendingUp
                                                    Screen.Portfolio -> Icons.Default.PieChart
                                                    Screen.Profile -> Icons.Default.Person
                                                },
                                                contentDescription = screen.name
                                            )
                                        }
                                    )
                                }
                            }
                        },
                        floatingActionButton = {
                            if (currentScreen == Screen.Portfolio) {
                                ExtendedFloatingActionButton(
                                    onClick = { mainViewModel.setShowAddTransaction(true) },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    icon = { Icon(Icons.Default.Add, null) },
                                    text = { Text("Add Transaction") }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (currentScreen) {
                                Screen.Market -> MarketScreen(
                                    onStockClick = { mainViewModel.setSelectedTicker(it) }
                                )
                                Screen.Portfolio -> PortfolioUi(
                                    onStockClick = { mainViewModel.setSelectedTicker(it) }
                                )
                                Screen.Profile -> UserUi(
                                    onLogout = { mainViewModel.logout() }
                                )
                            }
                            selectedTicker?.let { ticker ->
                                StockDetailScreen(
                                    ticker = ticker,
                                    onBack = { mainViewModel.clearSelectedTicker()

                                    }
                                )
                            }


                        }
                    }
                }
            }
        }
    }
}