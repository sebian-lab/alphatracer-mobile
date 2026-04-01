package com.example.alphatracer.data.model.request

/**
 * Request body for login endpoint.
 */
data class LoginRequest(
    val email: String,
    val password: String
)
