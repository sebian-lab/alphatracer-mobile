package com.main.alphatracer.network

import com.google.gson.annotations.SerializedName
import com.main.alphatracer.model.CandleResponse
import com.main.alphatracer.model.MarketAnalysisResponse
import com.main.alphatracer.model.MetricsResponse
import com.main.alphatracer.model.TransactionRequest
import com.main.alphatracer.model.TransactionResponse
import com.main.alphatracer.ui.Portfolio.PortfolioResponse
import com.main.alphatracer.ui.market.StockSearchResult
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // authentication
    @FormUrlEncoded
    @POST("api/v1/auth/login")
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): LoginResponse
    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(@Header("Authorization") refreshToken: String): LoginResponse
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): LoginResponse
    data class LoginResponse(
        val access_token: String,
        val refresh_token: String,
        val token_type: String
    )
    data class RegisterRequest(
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String,
        @SerializedName("full_name") val fullName: String
    )
    // primarly alert
    @GET("api/v1/market/{ticker}/candles/stored")
    suspend fun getCandlesByDateRange(
        @Path("ticker") ticker: String,
        @Query("interval") interval: String = "1d",
        @Query("start") startDate: String,   // yyyy-MM-dd
        @Query("end") endDate: String
    ): List<CandleResponse>
    // portfolio stuff
    @GET("api/v1/portfolio")
    suspend fun getPortfolio(@Header("Authorization") auth: String): PortfolioResponse
    @POST("api/v1/portfolio/transactions")
    suspend fun addTransaction(
        @Header("Authorization") auth: String,
        @Body transaction: TransactionRequest
    ): TransactionResponse


    //Stock
    @GET("api/v1/stocks/{ticker}/metrics")
    suspend fun getStockMetrics(@Path("ticker") ticker: String): MetricsResponse

    @GET("api/v1/market/{ticker}/analysis")
    suspend fun getMarketAnalysis(
        @Path("ticker") ticker: String,
        @Query("interval") interval: String = "1d",
        @Query("period") period: String = "3mo"
    ): MarketAnalysisResponse

    // search
    @GET("api/v1/stocks/search")
    suspend fun searchStocks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): List<StockSearchResult>
}
