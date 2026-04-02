package com.stock.alphatracer.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * User profile response model
 */
data class UserProfile(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("date_of_birth") val dateOfBirth: String? = null
)
