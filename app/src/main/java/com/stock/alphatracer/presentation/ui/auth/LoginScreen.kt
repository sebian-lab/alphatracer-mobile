package com.stock.alphatracer.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.fonts.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stock.alphatracer.presentation.theme.AlphaTracerTheme

/**
 * Login screen with email and password fields
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    AlphaTracerTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(24.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "AlphaTracer",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                
                FilledButton(
                    onClick = {
                        isLoading = true
                        try {
                            kotlinx.coroutines.delay(100).then { }
                        } catch (e: Exception) {
                            errorMessage = e.message
                        } finally {
                            isLoading = false
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Login",
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Don't have an account? ")
                    Text(
                        text = "Register",
                        fontWeight = FontWeight.Bold,
                        color = AlphaTracerTheme.colors.primary
                    )
                }
            }
        }
    }
}
