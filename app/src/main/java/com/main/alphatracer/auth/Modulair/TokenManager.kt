package com.main.alphatracer.auth.Modulair


// hoger
import android.content.Context
import android.content.SharedPreferences
import androidx.biometric.BiometricManager


class TokenManager private constructor(context: Context) {
    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences("auth", Context.MODE_PRIVATE)
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)
    fun isBiometricEnabled(): Boolean = prefs.getBoolean("biometric_enabled", false)
    fun isBiometricAvailable(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK
        return biometricManager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }
    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("biometric_enabled", enabled).apply()
    }
    fun saveToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun getToken(): String? = prefs.getString("access_token", null)



    companion object {
        @Volatile
        private var instance: TokenManager? = null

        /**
         * Must be called once, before any call to getInstance().
         * Typically in MainActivity.onCreate().
         */
        fun init(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = TokenManager(context.applicationContext)
                    }
                }
            }
        }

        fun getInstance(): TokenManager {
            return instance ?: throw IllegalStateException(
                "TokenManager not initialized. Call TokenManager.init(context) first."
            )
        }
    }
    fun getUserName(): String = prefs.getString("user_name", "User") ?: "User"
    fun getUserEmail(): String = prefs.getString("user_email", "") ?: ""


    fun saveUserDetails(token: String, refreshToken: String, name: String, email: String) {
        prefs.edit()
            .putString("access_token", token)
            .putString("refresh_token", refreshToken)
            .putString("user_name", name)
            .putString("user_email", email)
            .apply()
    }
    fun clear() {
        prefs.edit().clear().apply()
    }
}