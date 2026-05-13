package com.main.alphatracer.ui.Portfolio


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun PortfolioUi(
    onStockClick: (String) -> Unit,
    viewModel: PortfolioViewModel = viewModel(

    )
){
    LaunchedEffect(Unit) {
        viewModel.loadPortfolio()
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is PortfolioUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is PortfolioUiState.Success -> {
            val success = uiState as PortfolioUiState.Success
            PortfolioContent(
                holdings = success.holdings,
                totalValue = success.totalValue,
                onStockClick = onStockClick
            )
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
private fun PortfolioContent(
    holdings: List<Holding>,
    totalValue: Double,
    onStockClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
            HoldingItem(holding = holding, onClick = { onStockClick(holding.ticker) })
        }
    }
}

@Composable
private fun HoldingItem(holding: Holding, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(holding.ticker, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    holding.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,maxLines = 1, // Keep it on one line
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$${String.format("%.2f", holding.currentPrice)}",
                    fontWeight = FontWeight.SemiBold,
                    softWrap = false)
                Text(
                    "${holding.quantity} shares",
                    style = MaterialTheme.typography.bodySmall,
                    softWrap = false)
                val total = holding.quantity * holding.currentPrice
                Text(
                    "$${String.format("%,.2f", total)}",
                    style = MaterialTheme.typography.bodySmall,
                    softWrap = false)
            }
        }
    }
}