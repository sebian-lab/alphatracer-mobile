package com.stock.alphatracer.data.model.request

import com.google.gson.annotations.SerializedName

/**
 * Request body for user registration
 */
data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("confirmPassword") val confirmPassword: String
)
