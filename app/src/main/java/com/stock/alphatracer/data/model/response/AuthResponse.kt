package com.stock.alphatracer.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Response from authentication endpoints (login/register)
 */
data class AuthResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user_id") val userId: String = "",
    @SerializedName("token_type") val tokenType: String? = null
)
