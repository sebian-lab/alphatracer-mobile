package com.main.alphatracer.network

import com.google.gson.annotations.SerializedName
import com.main.alphatracer.model.CandleResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // ApiService.kt
    @FormUrlEncoded
    @POST("api/v1/auth/login")
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): LoginResponse

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
    @GET("api/v1/market/{ticker}/candles/stored")
    suspend fun getCandlesByDateRange(
        @Path("ticker") ticker: String,
        @Query("interval") interval: String = "1d",
        @Query("start") startDate: String,   // yyyy-MM-dd
        @Query("end") endDate: String
    ): List<CandleResponse>
}
