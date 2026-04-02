package com.stock.alphatracer.data.model.request

import com.google.gson.annotations.SerializedName

/**
 * Request body for user login
 */
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
