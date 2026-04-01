package com.example.alphatracer.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

/**
 * Manages authentication tokens in DataStore.
 */
class TokenManager(private val dataStore: DataStore<Preferences>) {
    
    private companion object Keys {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    /**
     * Save access and refresh tokens.
     */
    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String? = null,
        userId: String? = null
    ) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            refreshToken?.let { preferences[REFRESH_TOKEN_KEY] = it }
            userId?.let { preferences[USER_ID_KEY] = it }
        }.await()
    }

    /**
     * Get access token.
     */
    suspend fun getAccessToken(): String? {
        return dataStore.data.firstOrNull()?.get(ACCESS_TOKEN_KEY)
    }

    /**
     * Get refresh token.
     */
    suspend fun getRefreshToken(): String? {
        return dataStore.data.firstOrNull()?.get(REFRESH_TOKEN_KEY)
    }

    /**
     * Get user ID.
     */
    suspend fun getUserId(): String? {
        return dataStore.data.firstOrNull()?.get(USER_ID_KEY)
    }

    /**
     * Clear all tokens and user data.
     */
    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
        }.await()
    }
}
