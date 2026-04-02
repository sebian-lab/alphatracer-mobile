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
 * Buy/Sell modal bottom sheet
 */
@Composable
fun BuySellSheet(
    type: String = "Buy", // "Buy" or "Sell"
    onDismiss: () -> Unit,
    onConfirm: (quantity: Int, price: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var quantity by remember { mutableStateOf(1)
        .mutableStateOf(1)
    }
    var price by remember { mutableDoubleStateOf(0.0) }
    var isExpanded by remember { mutableStateOf(true) }
    
    val title = when (type) {
        "Buy" -> "Buy $quantity Shares"
        "Sell" -> "Sell $quantity Shares"
        else -> type
    }
    
    AlertDialog(
        modifier = modifier,
        icon = { Text(text = type, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                // Quantity
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Quantity", fontSize = 14.sp)
                    OutlinedTextField(
                        value = quantity.toString(),
                        onValueChange = { quantity = it.toIntOrNull() ?: 1 },
                        modifier = Modifier.width(80.dp),
                        minLines = 1,
                        singleLine = true
                    )
                }
                
                // Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Price", fontSize = 14.sp)
                    OutlinedTextField(
                        value = "\${price}",
                        onValueChange = { price = it.replace(Regex("\\D"), "")?.toDoubleOrNull() ?: 0.0 },
                        modifier = Modifier.width(80.dp),
                        minLines = 1,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(type = KeyboardType.Decimal)
                    )
                }
            }
        },
        onDismissRequest = { isExpanded = false; onDismiss() },
        confirmButton = {
            FilledTonalButton(
                onClick = { if (isExpanded) { isExpanded = false } else { onConfirm(quantity, price) } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("${if (type == "Buy") "Add" else "Sell"}", fontSize = 16.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
