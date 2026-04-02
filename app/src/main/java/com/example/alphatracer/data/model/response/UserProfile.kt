package com.example.alphatracer.data.model.response

import java.time.Instant

/**
 * User profile information.
 */
data class UserProfile(
    val id: String?,
    val email: String,
    val full_name: String?,
    val created_at: Instant? = null
)
