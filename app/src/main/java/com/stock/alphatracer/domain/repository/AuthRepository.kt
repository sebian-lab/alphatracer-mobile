package com.stock.alphatracer.domain.repository

import Result
import com.stock.alphatracer.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations
 */
interface AuthRepository {
    
    /**
     * Login with email and password
     */
    suspend fun login(email: String, password: String): Result<String>
    
    /**
     * Register new user
     */
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Unit>
    
    /**
     * Get current user profile
     */
    suspend fun getUserProfile(): Result<User>
    
    /**
     * Update user profile
     */
    suspend fun updateProfile(updates: User): Result<Unit>
    
    /**
     * Logout - clears tokens and local data
     */
    suspend fun logout()
}

