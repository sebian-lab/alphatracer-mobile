package com.example.alphatracer.data.model.request

/**
 * Request body for register endpoint.
 */
data class RegisterRequest(
    val email: String,
    val password: String,
    val full_name: String
)
