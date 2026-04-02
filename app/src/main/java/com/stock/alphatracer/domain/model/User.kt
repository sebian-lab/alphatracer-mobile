package com.stock.alphatracer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Domain model for User
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    var name: String,
    var email: String,
    var phone: String? = null,
    var dateOfBirth: String? = null
)
