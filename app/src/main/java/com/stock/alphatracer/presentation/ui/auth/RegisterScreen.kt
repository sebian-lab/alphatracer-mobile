package com.stock.alphatracer.presentation.ui.auth

import androidx.compose.foundation.layout.*
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
 * Register screen with name, email, password, and confirm password fields
 */
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
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
                
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                
                FilledButton(
                    onClick = {
                        isLoading = true
                        try {
                            // TODO: Implement register logic
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
                        text = "Register",
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Already have an account? ")
                    Text(
                        text = "Login",
                        fontWeight = FontWeight.Bold,
                        color = AlphaTracerTheme.colors.primary
                    )
                }
            }
        }
    }
}
