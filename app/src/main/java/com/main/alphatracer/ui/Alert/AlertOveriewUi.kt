package com.main.alphatracer.ui.Alert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.main.alphatracer.model.AlertRule
import com.main.alphatracer.ui.Alert.Data.AlertDataStore
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AlertListView(
    dataStore: AlertDataStore,
    onDelete: (AlertRule) -> Unit = {}
) {
    val rules by dataStore.getRulesFlow().collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()
    var ruleToEdit by remember { mutableStateOf<AlertRule?>(null) }
    ruleToEdit?.let { rule ->
        SetAlertDialog(
            ticker = rule.ticker,
            initialRule = rule,
            onDismiss = { ruleToEdit = null },
            onSave = { updatedRule ->
                scope.launch {
                    dataStore.updateRule(updatedRule)
                    ruleToEdit = null
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(rules, key = { it.id }) { rule ->
            AlertCard(
                rule = rule,
                onEdit = { ruleToEdit = rule }, // Pass the edit trigger
                onDelete = {
                    scope.launch { dataStore.deleteRule(rule.id) }
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertCard(
    rule: AlertRule,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        onClick = onEdit,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rule.ticker,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Alert")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Threshold: ↓ ${rule.thresholdPercent}%",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )

            // Updated to show Rolling Period instead of fixed dates
            Text(
                text = "Monitoring: Last ${rule.rollingDays} days (Rolling)",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (rule.isActive()) "● Active" else "○ Disabled",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (rule.isActive()) Color(0xFF4CAF50) else Color.Gray
                )
                Spacer(modifier = Modifier.width(12.dp))

                val lastTriggerText = if (rule.lastTriggeredAt == 0L) {
                    "Never"
                } else {
                    val dateTime = Instant.ofEpochMilli(rule.lastTriggeredAt)
                        .atZone(ZoneId.systemDefault())
                    dateTime.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))
                }

                Text(
                    text = "Last alert: $lastTriggerText",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}