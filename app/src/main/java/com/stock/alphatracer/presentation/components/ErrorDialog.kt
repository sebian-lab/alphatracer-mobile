package com.stock.alphatracer.presentation.components

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
 * Reusable error dialog with retry functionality
 */
@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(true) }
    
    if (showDialog) {
        AlertDialog(
            modifier = modifier,
            icon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Error,
                    contentDescription = null
                )
            },
            title = { Text(text = "Error") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            },
            onDismissRequest = { showDialog = false },
            actionsContainerColor = AlphaTracerTheme.colors.primary,
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Dismiss")
                }
            },
            confirmButton = onRetry?.let { retry ->
                Button(
                    onClick = { showDialog = false; retry() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text("Retry", fontWeight = FontWeight.Bold)
                }
            }
        )
    } else {
        onDismiss()
    }
}
