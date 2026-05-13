package com.main.alphatracer.ui.Alert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.main.alphatracer.model.AlertRule
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetAlertDialog(
    ticker: String,
    initialRule: AlertRule? = null,
    onDismiss: () -> Unit,
    onSave: (AlertRule) -> Unit
) {
    var rollingDays by remember { mutableStateOf("3") }
    var threshold by remember { mutableStateOf(initialRule?.thresholdPercent?.toString() ?: "5.0") }
    var useRolling by remember { mutableStateOf(initialRule == null) }

    // 1. Define the states for the pickers
    val startDateState = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate.now().toMillis()
    )
    val endDateState = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate.now().plusDays(3).toMillis()
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialRule == null) "Set Alert for $ticker" else "Edit Alert for $ticker") },
        text = {
            Column {

                Spacer(modifier = Modifier.height(8.dp))
                if (initialRule == null) {
                    Column {
                        Text("How many days in the past should we monitor?")
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = rollingDays,
                            onValueChange = { rollingDays = it },
                            label = { Text("Days back (Rolling)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = threshold,
                            onValueChange = { threshold = it },
                            label = { Text("Drop threshold (%)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                }
                if (useRolling && initialRule == null) {
                    OutlinedTextField(
                        value = rollingDays,
                        onValueChange = { rollingDays = it },
                        label = { Text("Days back") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                 else {

                    Text("Start Date")
                    DatePicker(state = startDateState, showModeToggle = false)

                    Text("End Date")
                    DatePicker(state = endDateState, showModeToggle = false)
                }


            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val days = rollingDays.toLongOrNull() ?: 2L
                    val rule = initialRule?.copy(
                        thresholdPercent = threshold.toDoubleOrNull() ?: 5.0,
                        rollingDays = days.toInt()
                    ) ?: AlertRule(
                        ticker = ticker,
                        rollingDays = days.toInt(),
                        thresholdPercent = threshold.toDoubleOrNull() ?: 5.0
                    )
                    onSave(rule)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
fun LocalDate.toMillis(): Long =
    this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

