package com.main.alphatracer.ui.StockUi.switch

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.main.alphatracer.model.MarketAnalysisResponse
import com.main.alphatracer.model.MetricsResponse
import com.main.alphatracer.ui.StockUi.MetricsGrid
import com.main.alphatracer.ui.StockUi.SignalCard
import com.main.alphatracer.ui.StockUi.TechnicalDetailsCard
import com.main.alphatracer.ui.StockUi.VolatilityVolumeCard

enum class StockDetailTab(val title: String) {
    Fundamental("Fundamental"),
    Analysis("Analysis"),
    Options("Options")
}
@Composable
fun AnalysisTypeSelector(
    selectedTab: StockDetailTab,
    onTabSelected: (StockDetailTab) -> Unit,
    metrics: MetricsResponse,
    analysis: MarketAnalysisResponse
) {
    // 3. Segment Selector Bar


        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            shape = RoundedCornerShape(50.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StockDetailTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    val backgroundTargetColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    }
                    val textTargetColor = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    // Smooth transition animations
                    val backgroundColor by androidx.compose.animation.animateColorAsState(
                        targetValue = backgroundTargetColor,
                        label = "tab_bg_color"
                    )
                    val textColor by androidx.compose.animation.animateColorAsState(
                        targetValue = textTargetColor,
                        label = "tab_text_color"
                    )

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        color = backgroundColor,
                        shape = RoundedCornerShape(50.dp),
                        onClick = { onTabSelected(tab) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = tab.title,
                                color = textColor,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }


    // 4. Dynamic Content Area (Changes based on selection)
    when (selectedTab) {
        StockDetailTab.Fundamental -> {
            Text("Key Metrics", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            MetricsGrid(metrics, analysis.quote)
        }
        StockDetailTab.Analysis -> {
            Text("Technical Analysis", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            TechnicalDetailsCard(analysis)
            SignalCard(analysis.signal)
        }
        StockDetailTab.Options -> {
            Text("Volatility & Volume", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            VolatilityVolumeCard(analysis)
        }
        }
    }
