package com.stock.alphatracer.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.fonts.FontWeight
import androidx.compose.ui.unit.dp
import com.stock.alphatracer.presentation.theme.AlphaTracerTheme

/**
 * Main authentication screen that shows either Login or Register based on state
 */
@Composable
fun AuthScreen(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authState by remember { mutableStateOf(AuthState()) }
    
    AlphaTracerTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color.White
        ) {
            when (authState) {
                is AuthState.Loading -> LoadingScreen()
                is AuthState.Success -> SuccessScreen(onNavigateToHome)
                else -> LoginRegisterScreen(authState, onNavigateToAuth)
            }
        }
    }
}

/**
 * Screen shown after successful login/register with navigation prompt
 */
@Composable
fun SuccessScreen(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
            .align(Alignment.Center),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome to AlphaTracer!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            FilledButton(onClick = onNavigateToHome) {
                Text("Go to Home")
            }
        }
    }
}

/**
 * Loading screen while authenticating
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
            .align(Alignment.Center),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = AlphaTracerTheme.colors.primary)
    }
}

/**
 * Screen showing both Login and Register options
 */
@Composable
fun LoginRegisterScreen(
    authState: AuthState,
    onNavigateToAuth: (String) -> Unit
) {
    val loginClick = { onNavigateToAuth("login") }
    val registerClick = { onNavigateToAuth("register") }
}
