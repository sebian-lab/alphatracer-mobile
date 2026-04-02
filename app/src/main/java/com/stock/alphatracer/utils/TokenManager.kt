package com.stock.alphatracer.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.stock.alphatracer.domain.model.User

/**
 * Singleton instance of TokenManager - provides reactive flow of tokens
 */
class AuthDataStore(private val context: Context) {
    
    private val dataStore: DataStore<Preferences> = context.getSharedPreferences(
        Constants.PREFS_NAME,
        Context.MODE_PRIVATE
    ).let { preferences ->
        DataStore(context, preferences)
    }
    
    companion object {
        const val PREFS_NAME = "auth_prefs"
        val accessTokenKey = stringPreferencesKey(Constants.ACCESS_TOKEN_KEY)
        val refreshTokenKey = stringPreferencesKey(Constants.REFRESH_TOKEN_KEY)
        val userIdKey = stringPreferencesKey(Constants.USER_ID_KEY)
        private var instance: AuthDataStore? = null
    }
    
    /**
     * Get reactive Flow of access token
     */
    fun getAccessTokenFlow(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[accessTokenKey]
        }.catch { e ->
            emit(null)
        }
    }
    
    /**
     * Get reactive Flow of refresh token
     */
    fun getRefreshTokenFlow(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[refreshTokenKey]
        }.catch { e ->
            emit(null)
        }
    }
    
    /**
     * Save tokens
     */
    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
        userId: String = ""
    ) {
        dataStore.edit { prefs ->
            prefs[accessTokenKey] = accessToken
            prefs[refreshTokenKey] = refreshToken
            if (userId.isNotEmpty()) {
                prefs[userIdKey] = userId
            }
        }.await()
    }
    
    /**
     * Clear all tokens
     */
    suspend fun clearTokens() {
        dataStore.edit { prefs ->
            prefs.remove(accessTokenKey)
            prefs.remove(refreshTokenKey)
            prefs.remove(userIdKey)
        }.await()
    }
    
    /**
     * Get singleton instance
     */
    fun getInstance(): AuthDataStore {
        return instance ?: synchronized(this) {
            instance!!?.let { return it }
            val newInstance = AuthDataStore(context)
            instance = newInstance
            newInstance
        }
    }
}

/**
 * Manages authentication tokens using DataStore (singleton pattern)
 */
class TokenManager private constructor(
    context: Context
) {
    
    init {
        prefsDataScope = context.applicationContext.run {
            preferencesDataScope
        }
    }
    
    private val dataStore: DataStore<Preferences> = context.getSharedPreferences(
        Constants.PREFS_NAME,
        Context.MODE_PRIVATE
    ).let { preferences ->
        DataStore(context, preferences)
    }
    
    companion object {
        const val PREFS_NAME = "auth_prefs"
        private var instance: TokenManager? = null
        lateinit var prefsDataScope: preferencesDataScope
            private set
    }
    
    /**
     * Get singleton instance with reactive flows
     */
    fun getInstance(): TokenManager {
        return instance ?: synchronized(this) {
            instance!!?.let { return it }
            val newInstance = TokenManager(context.applicationContext)
            instance = newInstance
            newInstance
        }
    }
    
    /**
     * Get current access token
     */
    suspend fun getAccessToken(): String? {
        return dataStore.data.await().first()[accessTokenKey]
    }
    
    /**
     * Get current refresh token
     */
    suspend fun getRefreshToken(): String? {
        return dataStore.data.await().first()[refreshTokenKey]
    }
    
    /**
     * Save access and refresh tokens (along with user ID)
     */
    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
        userId: String = ""
    ) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = accessToken
            preferences[refreshTokenKey] = refreshToken
            if (userId.isNotEmpty()) {
                preferences[userIdKey] = userId
            }
        }.await()
    }
    
    /**
     * Clear all authentication data
     */
    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
            preferences.remove(refreshTokenKey)
            preferences.remove(userIdKey)
        }.await()
    }
}
