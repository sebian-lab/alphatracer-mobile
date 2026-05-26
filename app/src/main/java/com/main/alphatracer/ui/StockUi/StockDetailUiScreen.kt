package com.main.alphatracer.ui.StockUi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.main.alphatracer.model.MarketAnalysisResponse
import com.main.alphatracer.model.MetricsResponse
import com.main.alphatracer.model.QuoteResponse
import com.main.alphatracer.model.SignalResponse
import com.main.alphatracer.ui.Alert.Data.AlertDataStore
import com.main.alphatracer.ui.Alert.SetAlertDialog
import com.main.alphatracer.ui.StockUi.switch.StockDetailTab
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.shader.ColorShader
import kotlinx.coroutines.launch
import com.main.alphatracer.ui.StockUi.switch.AnalysisTypeSelector
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailScreen(
    ticker: String,
    onBack: () -> Unit,
    onPortfolioRefresh: () -> Unit,
    viewModel: StockDetailViewModel = viewModel()
) {
    LaunchedEffect(ticker) {
        viewModel.loadStockDetail(ticker)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = ticker,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is StockDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is StockDetailUiState.Success -> {
                    val success = uiState as StockDetailUiState.Success
                    StockDetailContent(
                        metrics = success.metrics,
                        analysis = success.analysis,
                        onBuy = { quantity, price ->
                            viewModel.buyStock(ticker, quantity, price)
                            onPortfolioRefresh()               // <-- trigger refresh
                        },
                        onSell = { quantity, price ->
                            viewModel.sellStock(ticker, quantity, price)
                            onPortfolioRefresh()               // <-- trigger refresh
                        }
                    )
                }
                is StockDetailUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = (uiState as StockDetailUiState.Error).message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = { viewModel.loadStockDetail(ticker) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun TransactionDialog(
    ticker: String,
    type: String, // "buy" or "sell"
    currentPrice: Double,
    onDismiss: () -> Unit,
    onConfirm: (quantity: Int, price: Double) -> Unit
) {
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf(String.format("%.2f", currentPrice)) }
    var errors by remember { mutableStateOf(AddDialogErrors()) }
    val focusManager = LocalFocusManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "${type.uppercase()} $ticker", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it.filter(Char::isDigit); errors = errors.copy(quantity = null) },
                    label = { Text("Quantity") },
                    isError = errors.quantity != null,
                    supportingText = { if (errors.quantity != null) Text(errors.quantity!!) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it.replace(',', '.').filter { c -> c.isDigit() || c == '.' }
                            .let { if (it.count { c -> c == '.' } > 1) price else it }
                        errors = errors.copy(price = null)
                    },
                    label = { Text("Price per share (USD)") },
                    isError = errors.price != null,
                    supportingText = { if (errors.price != null) Text(errors.price!!) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    focusManager.clearFocus()
                    val q = quantity.toIntOrNull()
                    val p = price.toDoubleOrNull()
                    when {
                        quantity.isBlank() -> errors = errors.copy(quantity = "Required")
                        q == null || q <= 0 -> errors = errors.copy(quantity = "Positive number")
                        price.isBlank() -> errors = errors.copy(price = "Required")
                        p == null || p <= 0 -> errors = errors.copy(price = "Positive number")
                        else -> onConfirm(q, p)
                    }
                }
            ) {
                Text("Confirm ${type.uppercase()}")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        shape = RoundedCornerShape(28.dp)
    )
}
@Composable
fun BuySellActions(onBuyClick: () -> Unit, onSellClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = onBuyClick,
            modifier = Modifier.weight(1f).height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFF00E676)
            ),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF00E676))
        ) { Text("BUY", fontWeight = FontWeight.Bold, fontSize = 13.sp) }

        Button(
            onClick = onSellClick,
            modifier = Modifier.weight(1f).height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFFFF5252)
            ),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFFFF5252))
        ) { Text("SELL", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
    }
}

@Composable
fun StockDetailContent(
    metrics: MetricsResponse,
    analysis: MarketAnalysisResponse,
    onBuy: (quantity: Int, price: Double) -> Unit,
    onSell: (quantity: Int, price: Double) -> Unit
) {
    var selectedTab by remember { mutableStateOf(StockDetailTab.Analysis) }
    val quote = analysis.quote
    var showTransactionDialog by remember { mutableStateOf(false) }
    var transactionType by remember { mutableStateOf("buy") }
    var showAlertDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val alertDataStore = remember { AlertDataStore(context) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = quote.ticker,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = quote.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant ,
                            maxLines = 1, // Keep the name on one line
                            overflow = TextOverflow.Ellipsis // Add "..." if it's too long
                        )
                    }
                    Column(modifier = Modifier.widthIn(min = 100.dp),horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${quote.currency} ${String.format("%.2f", quote.price)}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (quote.changePct >= 0)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = "${if (quote.changePct >= 0) "+" else ""}${String.format("%.2f", quote.changePct)}%",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Bold,
                                color = if (quote.changePct >= 0)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }


        item {

            val modelProducer = remember { CartesianChartModelProducer() }


            LaunchedEffect(analysis.candles) {
                if (analysis.candles.isNotEmpty()) {
                    modelProducer.runTransaction {
                        lineSeries {
                            series(analysis.candles.map { it.close })
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Price History (${analysis.interval})",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3. The Chart Host
                    CartesianChartHost(
                        chart = rememberCartesianChart(
                            rememberLineCartesianLayer(
                                lines = listOf(
                                    rememberLineSpec(
                                        // Use ColorShader instead of DynamicShaders
                                        shader = ColorShader(MaterialTheme.colorScheme.primary.toArgb()),
                                        backgroundShader = null
                                    )
                                )
                            ),
                            startAxis = rememberStartAxis(
                                label = rememberAxisLabelComponent(textSize = 10.sp),
                                guideline = rememberAxisGuidelineComponent(
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            ),
                            bottomAxis = rememberBottomAxis(
                                label = rememberAxisLabelComponent(textSize = 10.sp),
                                guideline = null
                            ),
                        ),
                        modelProducer = modelProducer,
                        modifier = Modifier.fillMaxSize(),
                        zoomState = rememberVicoZoomState(
                            zoomEnabled = true,
                            initialZoom = Zoom.Content
                        )
                    )

                }
            }
        }

        item {
            // Logic moved to analyzeUi package
            AnalysisTypeSelector(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                metrics = metrics,
                analysis = analysis
            )
        }





        item { Spacer(modifier = Modifier.height(8.dp)) }

        item { Button(
            onClick = { showAlertDialog = true },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Set Alert Test")
        }
        }

    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        BuySellActions(
            modifier = Modifier.padding(12.dp),
            onBuyClick = {
                transactionType = "buy"
                showTransactionDialog = true
            },
            onSellClick = {
                transactionType = "sell"
                showTransactionDialog = true
            }
        )
    }

    if (showTransactionDialog) {
        TransactionDialog(
            ticker = quote.ticker,
            type = transactionType,
            currentPrice = quote.price,
            onDismiss = { showTransactionDialog = false },
            onConfirm = { quantity, price ->
                if (transactionType == "buy") {
                    onBuy(quantity, price)
                } else {
                    onSell(quantity, price)
                }
                showTransactionDialog = false
            }
        )
    }




    if (showAlertDialog) {
        SetAlertDialog(
            ticker = quote.ticker,
            onDismiss = { showAlertDialog = false },
            onSave = { rule ->
                scope.launch { alertDataStore.addRule(rule) }
                showAlertDialog = false
            }
        )

    }
}



@Composable
fun MetricsGrid(metrics: MetricsResponse, quote: QuoteResponse) {
    val metricItems = listOf(
        "Market Cap" to formatLargeNumber(metrics.marketCap),
        "P/E Ratio" to formatDouble(metrics.peRatio),
        "Forward P/E" to formatDouble(metrics.forwardPe),
        "PEG Ratio" to formatDouble(metrics.pegRatio),
        "Price/Book" to formatDouble(metrics.priceToBook),
        "Price/Sales" to formatDouble(metrics.priceToSales),
        "EPS (TTM)" to formatDouble(metrics.epsTtm),
        "EPS (Forward)" to formatDouble(metrics.epsForward),
        "Book Value" to formatDouble(metrics.bookValue),
        "Dividend Yield" to "${String.format("%.2f", metrics.dividendYield)}%",
        "Beta" to formatDouble(metrics.beta),
        "52W High" to formatDouble(quote.week52High),
        "52W Low" to formatDouble(quote.week52Low),
        "Avg Volume" to formatLargeNumber(quote.avgVolume.toDouble()),
        "Gross Margin" to "${String.format("%.2f", metrics.grossMargin)}%",
        "Operating Margin" to "${String.format("%.2f", metrics.operatingMargin)}%",
        "Net Margin" to "${String.format("%.2f", metrics.netMargin)}%",
        "ROE" to "${String.format("%.2f", metrics.roe)}%",
        "ROA" to "${String.format("%.2f", metrics.roa)}%",
        "ROI" to "${String.format("%.2f", metrics.roi)}%",
        "Debt/Equity" to formatDouble(metrics.debtToEquity),
        "Current Ratio" to formatDouble(metrics.currentRatio),
        "Quick Ratio" to formatDouble(metrics.quickRatio),
        "Revenue Growth (YoY)" to "${String.format("%.2f", metrics.revenueGrowthYoy)}%",
        "Earnings Growth (YoY)" to "${String.format("%.2f", metrics.earningsGrowthYoy)}%"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            metricItems.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { (label, value) ->
                        StatItem(
                            label = label,
                            value = value,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TechnicalDetailsCard(analysis: MarketAnalysisResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader(title = "Moving Averages")
            ResponsiveMetricRow(
                items = listOf(
                    "SMA 20" to formatDouble(analysis.ma.sma20),
                    "SMA 50" to formatDouble(analysis.ma.sma50),
                    "SMA 200" to formatDouble(analysis.ma.sma200),
                    "EMA 9" to formatDouble(analysis.ma.ema9),
                    "EMA 21" to formatDouble(analysis.ma.ema21),
                    "EMA 50" to formatDouble(analysis.ma.ema50),
                    "VWAP" to formatDouble(analysis.ma.vwap)
                )
            )

            Divider()

            SectionHeader(title = "Oscillators")
            ResponsiveMetricRow(
                items = listOf(
                    "RSI (14)" to formatDouble(analysis.oscillators.rsi14),
                    "Stoch %K" to formatDouble(analysis.oscillators.stochK),
                    "Stoch %D" to formatDouble(analysis.oscillators.stochD),
                    "CCI (20)" to formatDouble(analysis.oscillators.cci20),
                    "Williams %R" to formatDouble(analysis.oscillators.williamsR),
                    "ADX" to formatDouble(analysis.oscillators.adx),
                    "DI+" to formatDouble(analysis.oscillators.diPlus),
                    "DI-" to formatDouble(analysis.oscillators.diMinus),
                    "MACD" to formatDouble(analysis.oscillators.macd),
                    "MACD Signal" to formatDouble(analysis.oscillators.macdSignal),
                    "MACD Hist" to formatDouble(analysis.oscillators.macdHist)
                )
            )
        }
    }
}

@Composable
fun VolatilityVolumeCard(analysis: MarketAnalysisResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader(title = "Bollinger Bands")
            ResponsiveMetricRow(
                items = listOf(
                    "Upper" to formatDouble(analysis.volatility.bbUpper),
                    "Middle" to formatDouble(analysis.volatility.bbMiddle),
                    "Lower" to formatDouble(analysis.volatility.bbLower),
                    "Width" to formatDouble(analysis.volatility.bbWidth),
                    "%B" to formatDouble(analysis.volatility.bbPctB),
                    "ATR (14)" to formatDouble(analysis.volatility.atr14)
                )
            )

            Divider()

            SectionHeader(title = "Volume")
            ResponsiveMetricRow(
                items = listOf(
                    "OBV" to formatLargeNumber(analysis.volume.obv),
                    "Volume" to formatLargeNumber(analysis.volume.volume.toDouble()),
                    "Rel Volume" to formatDouble(analysis.volume.relVolume)
                )
            )
        }
    }
}

@Composable
fun SignalCard(signal: SignalResponse) {
    // 1. Define high-visibility "Trading" colors
    val signalColor = when (signal.rating.lowercase()) {
        "buy" -> Color(0xFF00E676)  // Vibrant Electric Green
        "sell" -> Color(0xFFFF5252) // Neon Action Red
        else -> MaterialTheme.colorScheme.tertiary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .graphicsLayer {
                // Subtle 3D lift
                shadowElevation = 12f
                shape = RoundedCornerShape(20.dp)
                clip = true
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            // Darker background makes the neon colors "glow"
            containerColor = Color(0xFF121212)
        ),
        // Add a colored border to make it feel like it's "framed" by the signal
        border = BorderStroke(1.dp, signalColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Glowing Icon/Text combo
                Icon(
                    imageVector = if (signal.rating.lowercase() == "buy")
                        Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = signalColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = signal.rating.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp,
                    color = signalColor
                )

                Spacer(modifier = Modifier.weight(1f))

                // Floating Score Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = signalColor,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "${String.format("%.1f", signal.score)}/10",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black // High contrast text on the bright badge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row with high-tech separators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SignalStat("BUY", signal.buySignals, Color(0xFF00E676))
                SignalStat("SELL", signal.sellSignals, Color(0xFFFF5252))
                SignalStat("NEUTRAL", signal.neutralSignals, Color.Gray)
            }

            Divider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = Color.White.copy(alpha = 0.1f)
            )

            Text(
                "ANALYSIS LOG",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color.Gray,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Detailed signals with a subtle "code" look
            signal.signals.forEach { (key, value) ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(
                        text = "> $key:",
                        style = MaterialTheme.typography.bodySmall,
                        color = signalColor.copy(alpha = 0.7f),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun SignalStat(label: String, value: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = Color.Gray)
        Text(text = value.toString(), fontWeight = FontWeight.Bold, color = color, fontSize = 16.sp)
    }
}


@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun ResponsiveMetricRow(items: List<Pair<String, String>>) {
    var remaining = items
    while (remaining.isNotEmpty()) {
        val rowItems = remaining.take(3)
        remaining = remaining.drop(3)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            rowItems.forEach { (label, value) ->
                StatItem(
                    label = label,
                    value = value,
                    modifier = Modifier.weight(1f)
                )
            }
            repeat(3 - rowItems.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun StatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
        Text(
            text = value.ifEmpty { "—" },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun AddToPortfolioDialog(
    ticker: String,
    onDismiss: () -> Unit,
    onAdd: (quantity: Int, price: Double) -> Unit
) {
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf(AddDialogErrors()) }
    val focusManager = LocalFocusManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add $ticker",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        quantity = it.filter { char -> char.isDigit() }
                        errors = errors.copy(quantity = null)
                    },
                    label = { Text("Quantity") },
                    isError = errors.quantity != null,
                    supportingText = {
                        if (errors.quantity != null) {
                            Text(errors.quantity!!, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it.replace(',', '.').filter { char ->
                            char.isDigit() || char == '.'
                        }.let { filtered ->
                            if (filtered.count { it == '.' } > 1) price else filtered
                        }
                        errors = errors.copy(price = null)
                    },
                    label = { Text("Price per share (${quoteCurrencySymbol()})") },
                    isError = errors.price != null,
                    supportingText = {
                        if (errors.price != null) {
                            Text(errors.price!!, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    focusManager.clearFocus()
                    val q = quantity.toIntOrNull()
                    val p = price.toDoubleOrNull()
                    when {
                        quantity.isBlank() -> errors = errors.copy(quantity = "Quantity is required")
                        q == null || q <= 0 -> errors = errors.copy(quantity = "Must be a positive number")
                        price.isBlank() -> errors = errors.copy(price = "Price is required")
                        p == null || p <= 0 -> errors = errors.copy(price = "Must be a positive number")
                        else -> {
                            onAdd(q, p)
                            onDismiss()
                        }
                    }
                }
            ) {
                Text("Add to Portfolio", fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(28.dp)
    )
}

private data class AddDialogErrors(
    val quantity: String? = null,
    val price: String? = null
)

@Composable
private fun quoteCurrencySymbol(): String = "$"

private fun formatDouble(value: Double?): String =
    if (value != null) String.format("%.2f", value) else "—"

private fun formatLargeNumber(value: Double): String = when {
    value >= 1_000_000_000_000 -> String.format("%.2fT", value / 1_000_000_000_000)
    value >= 1_000_000_000 -> String.format("%.2fB", value / 1_000_000_000)
    value >= 1_000_000 -> String.format("%.2fM", value / 1_000_000)
    else -> String.format("%.0f", value)
}