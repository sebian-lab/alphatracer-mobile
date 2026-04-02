package com.example.alphatracer.data.model.response

/**
 * Response containing authentication tokens.
 */
data class AuthResponse(
    val access_token: String,
    val refresh_token: String?
)
