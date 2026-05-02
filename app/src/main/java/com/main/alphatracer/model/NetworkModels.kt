package com.main.alphatracer.model

import com.google.gson.annotations.SerializedName

data class MetricsResponse(
    val ticker: String,
    @SerializedName("current_price") val currentPrice: Double,
    @SerializedName("market_cap") val marketCap: Double,
    @SerializedName("pe_ratio") val peRatio: Double,
    @SerializedName("forward_pe") val forwardPe: Double,
    @SerializedName("peg_ratio") val pegRatio: Double,
    @SerializedName("price_to_book") val priceToBook: Double,
    @SerializedName("price_to_sales") val priceToSales: Double,
    @SerializedName("eps_ttm") val epsTtm: Double,
    @SerializedName("eps_forward") val epsForward: Double,
    @SerializedName("book_value") val bookValue: Double,
    @SerializedName("dividend_yield") val dividendYield: Double,
    @SerializedName("gross_margin") val grossMargin: Double,
    @SerializedName("operating_margin") val operatingMargin: Double,
    @SerializedName("net_margin") val netMargin: Double,
    @SerializedName("roe") val roe: Double,
    @SerializedName("roa") val roa: Double,
    @SerializedName("roi") val roi: Double,
    @SerializedName("current_ratio") val currentRatio: Double,
    @SerializedName("quick_ratio") val quickRatio: Double,
    @SerializedName("debt_to_equity") val debtToEquity: Double,
    @SerializedName("revenue_growth_yoy") val revenueGrowthYoy: Double,
    @SerializedName("earnings_growth_yoy") val earningsGrowthYoy: Double,
    val beta: Double,
    @SerializedName("week_52_high") val week52High: Double,
    @SerializedName("week_52_low") val week52Low: Double,
    @SerializedName("avg_volume") val avgVolume: Double,
    @SerializedName("fetched_at") val fetchedAt: String
)


data class MarketAnalysisResponse(
    val quote: QuoteResponse,
    val candles: List<CandleResponse>,
    val ma: MovingAveragesResponse,
    val oscillators: OscillatorsResponse,
    val volatility: VolatilityResponse,
    val volume: VolumeAnalysisResponse,
    val signal: SignalResponse,
    val interval: String,
    val period: String,
    @SerializedName("bars_saved") val barsSaved: Int
)

data class QuoteResponse(
    val ticker: String,
    val name: String,
    val exchange: String,
    val currency: String,
    val price: Double,
    val open: Double,
    val high: Double,
    val low: Double,
    @SerializedName("prev_close") val prevClose: Double,
    val volume: Long,
    @SerializedName("avg_volume") val avgVolume: Double,
    val change: Double,
    @SerializedName("change_pct") val changePct: Double,
    @SerializedName("amplitude_pct") val amplitudePct: Double,
    @SerializedName("market_cap") val marketCap: Double,
    @SerializedName("week_52_high") val week52High: Double,
    @SerializedName("week_52_low") val week52Low: Double,
    @SerializedName("fetched_at") val fetchedAt: String
)

data class CandleResponse(
    val datetime: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
)

data class MovingAveragesResponse(
    @SerializedName("sma_20") val sma20: Double?,
    @SerializedName("sma_50") val sma50: Double?,
    @SerializedName("sma_200") val sma200: Double?,
    @SerializedName("ema_9") val ema9: Double?,
    @SerializedName("ema_21") val ema21: Double?,
    @SerializedName("ema_50") val ema50: Double?,
    val vwap: Double?
)

data class OscillatorsResponse(
    @SerializedName("rsi_14") val rsi14: Double?,
    @SerializedName("stoch_k") val stochK: Double?,
    @SerializedName("stoch_d") val stochD: Double?,
    @SerializedName("cci_20") val cci20: Double?,
    @SerializedName("williams_r") val williamsR: Double?,
    val macd: Double?,
    @SerializedName("macd_signal") val macdSignal: Double?,
    @SerializedName("macd_hist") val macdHist: Double?,
    val adx: Double?,
    @SerializedName("di_plus") val diPlus: Double?,
    @SerializedName("di_minus") val diMinus: Double?
)

data class VolatilityResponse(
    @SerializedName("bb_upper") val bbUpper: Double,
    @SerializedName("bb_middle") val bbMiddle: Double,
    @SerializedName("bb_lower") val bbLower: Double,
    @SerializedName("bb_width") val bbWidth: Double,
    @SerializedName("bb_pct_b") val bbPctB: Double,
    @SerializedName("atr_14") val atr14: Double
)

data class VolumeAnalysisResponse(
    val obv: Double,
    val volume: Long,
    @SerializedName("avg_volume") val avgVolume: Double,
    @SerializedName("rel_volume") val relVolume: Double
)

data class SignalResponse(
    val rating: String,
    val score: Double,
    @SerializedName("buy_signals") val buySignals: Int,
    @SerializedName("sell_signals") val sellSignals: Int,
    @SerializedName("neutral_signals") val neutralSignals: Int,
    val signals: Map<String, String>
    )
