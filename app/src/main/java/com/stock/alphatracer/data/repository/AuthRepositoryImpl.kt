package com.stock.alphatracer.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.stock.alphatracer.domain.model.User
import com.stock.alphatracer.domain.repository.AuthRepository
import com.stock.alphatracer.utils.Constants
import com.stock.alphatracer.utils.TokenManager
import kotlinx.coroutines.flow.first
import Result

/**
 * Implementation of AuthRepository for authentication operations.
 * Uses API calls combined with local DataStore caching.
 */
class AuthRepositoryImpl(
    private val context: Context,
    private val tokenManager: TokenManager = TokenManager.getInstance()
) : AuthRepository {

    companion object {
        private const val PREFS_NAME = "auth_prefs"
    }

    init {
        // Initialize DataStore and TokenManager
        val dataStore: DataStore<Preferences> = context.dataStore(PREFS_NAME)
        tokenManager.setContext(context, dataStore)
    }

    /**
     * Login with email and password.
     * Stores access token in DataStore for future API calls.
     */
    override suspend fun login(email: String, password: String): Result<String> {
        // TODO: Replace with actual API call to /login endpoint
        return Result.failure(Exception("Login not implemented yet"))
    }

    /**
     * Register new user account.
     */
    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Unit> {
        // TODO: Replace with actual API call to /register endpoint
        return Result.failure(Exception("Register not implemented yet"))
    }

    /**
     * Get current user profile from local cache.
     * Falls back to API if needed in future implementation.
     */
    override suspend fun getUserProfile(): Result<User> {
        try {
            // Return a default User since we don't have a mappers yet
            return Result.success(User(
                id = "1",
                name = "User",
                email = "user@example.com"
            ))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    /**
     * Update user profile with new data.
     */
    override suspend fun updateProfile(updates: User): Result<Unit> {
        try {
            // TODO: Implement API call to update user profile
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    /**
     * Logout - clears tokens and local data.
     */
    override suspend fun logout(): Result<Unit> {
        tokenManager.clearTokens()
        return Result.success(Unit)
    }
}
