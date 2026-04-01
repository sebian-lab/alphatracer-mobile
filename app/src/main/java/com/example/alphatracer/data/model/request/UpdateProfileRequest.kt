package com.example.alphatracer.data.model.request

/**
 * Request body for updating user profile.
 */
data class UpdateProfileRequest(
    val email: String?,
    val full_name: String?
)
