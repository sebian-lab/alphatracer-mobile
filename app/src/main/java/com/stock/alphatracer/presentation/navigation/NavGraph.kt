package com.stock.alphatracer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.stock.alphatracer.presentation.ui.auth.LoginScreen
import com.stock.alphatracer.presentation.ui.auth.RegisterScreen
import com.stock.alphatracer.presentation.ui.home.HomeScreen
import com.stock.alphatracer.presentation.ui.search.SearchScreen
import com.stock.alphatracer.presentation.ui.portfolio.PortfolioScreen
import com.stock.alphatracer.presentation.ui.watchlist.WatchlistScreen
import com.stock.alphatracer.presentation.ui.settings.SettingsScreen
import com.stock.alphatracer.presentation.ui.stockdetail.StockDetailScreen

/**
 * Main navigation graph for all screens in the app
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Destinations = Destinations.Login
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Login Screen
        composable(route = Destinations.Login.route) {
            LoginScreen(onLoginSuccess = { })
        }
        
        // Register Screen
        composable(route = Destinations.Register.route) {
            RegisterScreen(onRegisterSuccess = { navController.navigate(Destinations.Home.route) })
        }
        
        // Home Screen
        composable(route = Destinations.Home.route) {
            HomeScreen()
        }
        
        // Search Screen
        composable(route = Destinations.Search.route) {
            SearchScreen(onSelectStock = { navController.navigate(it) })
        }
        
        // Stock Detail Screen
        composable(
            route = "${Destinations.StockDetail.route}",
            arguments = listOf(navArgument("ticker") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticker = backStackEntry.arguments?.getString("ticker") ?: ""
            StockDetailScreen(ticker = ticker)
        }
        
        // Portfolio Screen
        composable(route = Destinations.Portfolio.route) {
            PortfolioScreen()
        }
        
        // Watchlist Screen
        composable(route = Destinations.Watchlist.route) {
            WatchlistScreen(onSelectStock = { navController.navigate(it) })
        }
        
        // Settings Screen
        composable(route = Destinations.Settings.route) {
            SettingsScreen(
                onLogout = { 
                    navController.popBackStack()
                    navController.navigate(Destinations.Login.route)
                },
                onSelectStock = { navController.navigate(it) }
            )
        }
    }
}
