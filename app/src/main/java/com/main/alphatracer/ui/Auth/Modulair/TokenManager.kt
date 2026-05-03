package com.main.alphatracer.ui.Auth.Modulair



import android.content.Context
import android.content.SharedPreferences

class TokenManager private constructor(context: Context) {
    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun getToken(): String? = prefs.getString("access_token", null)



    companion object {
        @Volatile
        private var instance: TokenManager? = null

        fun getInstance(context: Context? = null): TokenManager {
            return instance ?: synchronized(this) {
                instance ?: TokenManager(context!!).also { instance = it }
            }
        }
    }
    fun getUserName(): String = prefs.getString("user_name", "User") ?: "User"
    fun getUserEmail(): String = prefs.getString("user_email", "") ?: ""

    fun saveUserDetails(token: String, name: String, email: String) {
        prefs.edit()
            .putString("access_token", token)
            .putString("user_name", name)
            .putString("user_email", email)
            .apply()
    }
    fun clear() {
        prefs.edit().clear().apply()
    }
}