package com.main.alphatracer.ui.Alert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    onDismiss: () -> Unit,
    onSave: (AlertRule) -> Unit
) {
    var rollingDays by remember { mutableStateOf("3") }
    var threshold by remember { mutableStateOf("5.0") }
    var useRolling by remember { mutableStateOf(true) }

    // 1. Define the states for the pickers
    val startDateState = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate.now().toMillis()
    )
    val endDateState = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate.now().plusDays(3).toMillis()
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Alert for $ticker") },
        text = {
            Column {
                Column() {
                    RadioButton(selected = useRolling, onClick = { useRolling = true })
                    Text("Rolling days back", modifier = Modifier.padding(end = 16.dp))
                    RadioButton(selected = !useRolling, onClick = { useRolling = false })
                    Text("Fixed date range")
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (useRolling) {
                    OutlinedTextField(
                        value = rollingDays,
                        onValueChange = { rollingDays = it },
                        label = { Text("Days back (e.g., 3)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                } else {

                    Text("Start Date")
                    DatePicker(state = startDateState, showModeToggle = false)

                    Text("End Date")
                    DatePicker(state = endDateState, showModeToggle = false)
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = threshold,
                    onValueChange = { threshold = it },
                    label = { Text("Drop threshold (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val days = rollingDays.toIntOrNull() ?: 3
                    val now = LocalDate.now()


                    val selectedStart = startDateState.selectedDateMillis?.toLocalDate() ?: now
                    val selectedEnd = endDateState.selectedDateMillis?.toLocalDate() ?: now

                    val (start, end) = if (useRolling) {
                        now.minusDays(days.toLong()) to now
                    } else {
                        selectedStart to selectedEnd
                    }

                    onSave(AlertRule(
                        ticker = ticker,
                        startDate = start,
                        endDate = end,
                        thresholdPercent = threshold.toDoubleOrNull() ?: 5.0
                    ))
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

fun Long.toLocalDate(): LocalDate =
    java.time.Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
