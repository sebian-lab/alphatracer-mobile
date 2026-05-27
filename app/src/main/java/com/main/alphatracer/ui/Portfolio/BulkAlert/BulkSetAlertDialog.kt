package com.main.alphatracer.ui.Portfolio.BulkAlert


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun BulkSetAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: (rollingDays: Int, thresholdPercent: Double) -> Unit
) {
    var rollingDays by remember { mutableStateOf("3") }
    var threshold by remember { mutableStateOf("5.0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Alert for Selected Stocks") },
        text = {
            Column {
                OutlinedTextField(
                    value = rollingDays,
                    onValueChange = { rollingDays = it },
                    label = { Text("Days to look back (rolling)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = threshold,
                    onValueChange = { threshold = it },
                    label = { Text("Drop threshold (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val days = rollingDays.toIntOrNull() ?: 3
                    val percent = threshold.toDoubleOrNull() ?: 5.0
                    onConfirm(days, percent)
                }
            ) { Text("Set Alerts") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}