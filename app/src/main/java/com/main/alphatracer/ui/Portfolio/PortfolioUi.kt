package com.main.alphatracer.ui.Portfolio


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.main.alphatracer.model.AlertRule
import com.main.alphatracer.ui.Alert.Data.AlertDataStore
import com.main.alphatracer.ui.Portfolio.BulkAlert.BulkSetAlertDialog
import com.stock.alphatracer.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@Composable
fun PortfolioUi(
    onStockClick: (String) -> Unit,
    alertDataStore: AlertDataStore,
    viewModel: PortfolioViewModel = viewModel(

    )
){
    val context = LocalContext.current
    val mainViewModel: MainViewModel = viewModel()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.loadPortfolio()
    }
    LaunchedEffect(Unit) {
        mainViewModel.refreshPortfolio.collect {
            viewModel.loadPortfolio()
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val existingRules by alertDataStore.getRulesFlow().collectAsState(initial = emptyList())

    var selectedTickers by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showBulkDialog by remember { mutableStateOf(false) }
    var selectionMode by remember { mutableStateOf(false) }  // Toggle for selection UI
    when (uiState) {
        is PortfolioUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is PortfolioUiState.Success -> {
            val success = uiState as PortfolioUiState.Success
            val holdings = success.holdings

            Column(modifier = Modifier.fillMaxSize()) {
                IconButton(onClick = { selectionMode = !selectionMode }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = if (selectionMode) "Cancel selection" else "Select stocks"
                    )
                }
                // Header row (Select All + Set Alert button)
                if (selectionMode) {
                    SelectionHeader(
                        holdings = holdings,
                        selectedTickers = selectedTickers,
                        onSelectAll = { isChecked ->
                            selectedTickers = if (isChecked) holdings.map { it.ticker }.toSet() else emptySet()
                        },
                        onSetAlertClick = { showBulkDialog = true },
                        isSetAlertEnabled = selectedTickers.isNotEmpty()
                    )
                }

                // Portfolio list with selection support
                PortfolioContent(
                    holdings = holdings,
                    totalValue = success.totalValue,
                    selectedTickers = selectedTickers,
                    existingRules = existingRules,
                    selectionMode = selectionMode,
                    onToggleSelection = { ticker ->
                        selectedTickers = if (selectedTickers.contains(ticker))
                            selectedTickers - ticker
                        else
                            selectedTickers + ticker
                    },
                    onStockClick = onStockClick
                )
            }

            if (showBulkDialog) {
                BulkSetAlertDialog(
                    onDismiss = { showBulkDialog = false },
                    onConfirm = { days, percent ->
                        scope.launch {
                            var successCount = 0
                            selectedTickers.forEach { ticker ->
                                val existingRule = existingRules.find { it.ticker == ticker }
                                val newRule = if (existingRule != null) {
                                    existingRule.copy(rollingDays = days, thresholdPercent = percent)
                                } else {
                                    AlertRule(
                                        ticker = ticker,
                                        rollingDays = days,
                                        thresholdPercent = percent
                                    )
                                }
                                if (existingRule != null) {
                                    alertDataStore.updateRule(newRule)
                                } else {
                                    alertDataStore.addRule(newRule)
                                }
                                Toast.makeText(context, "Alert set for $ticker", Toast.LENGTH_SHORT).show()
                                successCount++
                            }
                            Toast.makeText(context, "✅ Alerts set for $successCount stocks", Toast.LENGTH_LONG).show()
                            showBulkDialog = false
                            selectedTickers = emptySet()
                            selectionMode = false
                        }
                    }
                )
            }
        }
        is PortfolioUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${(uiState as PortfolioUiState.Error).message}")
                    Button(onClick = { viewModel.loadPortfolio() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
@Composable
private fun SelectionHeader(
    holdings: List<Holding>,
    selectedTickers: Set<String>,
    onSelectAll: (Boolean) -> Unit,
    onSetAlertClick: () -> Unit,
    isSetAlertEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = selectedTickers.size == holdings.size && holdings.isNotEmpty(),
                onCheckedChange = onSelectAll
            )
            Text("Select All", modifier = Modifier.padding(start = 4.dp))
        }
        Button(
            onClick = onSetAlertClick,
            enabled = isSetAlertEnabled
        ) {
            Text("Set Alert (${selectedTickers.size})")
        }
    }
}
@Composable
private fun PortfolioContent(
    holdings: List<Holding>,
    totalValue: Double,
    selectedTickers: Set<String>,
    existingRules: List<AlertRule>,
    selectionMode: Boolean,
    onToggleSelection: (String) -> Unit,
    onStockClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Portfolio Value", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "$${String.format("%,.2f", totalValue)}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        items(holdings) { holding ->
            HoldingItem(
                holding = holding,
                hasAlert = existingRules.any { it.ticker == holding.ticker },
                isSelected = selectedTickers.contains(holding.ticker),
                selectionMode = selectionMode,
                onToggleSelection = { onToggleSelection(holding.ticker) },
                onClick = { onStockClick(holding.ticker) }
            )
        }
    }
}
@Composable
private fun HoldingItem(
        holding: Holding,
        hasAlert: Boolean,
        isSelected: Boolean,
        selectionMode: Boolean,
        onToggleSelection: () -> Unit,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectionMode) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { onToggleSelection() }
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        holding.ticker,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    if (hasAlert) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.CheckCircle,
                            contentDescription = "Alert set",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        "${holding.quantity} | $${String.format("%,.2f", holding.currentPrice)}",
                        color = Color.Gray, fontSize = 14.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "$${String.format("%,.2f", holding.quantity * holding.currentPrice)}",
                        color = Color.White, fontWeight = FontWeight.Bold
                    )
                    // Assuming you add a 'change' field to your Holding model
                    Text(
                        text = "${if (holding.gainLossPct >= 0) "+" else ""}${
                            String.format(
                                "%.2f",
                                holding.gainLossPct
                            )
                        }%",
                        color = if (holding.gainLossPct >= 0) Color(0xFF00E676) else Color(
                            0xFFFF5252
                        ),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }



