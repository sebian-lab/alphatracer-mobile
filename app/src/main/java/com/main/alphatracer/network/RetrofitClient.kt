package com.main.alphatracer.network

import com.main.alphatracer.Auth.Modulair.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://twelve-sale-asp-last.trycloudflare.com/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(AuthInterceptor())
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    private class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val token = TokenManager.getInstance().getToken()
            if (token != null) {
                request = request.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            }
            val response = chain.proceed(request)
            if (response.code == 401) {
                synchronized(this) {
                    val newToken = refreshAccessToken()
                    if (newToken != null) {
                        val newRequest = request.newBuilder()
                            .header("Authorization", "Bearer $newToken")
                            .build()
                        return chain.proceed(newRequest)
                    }
                }
            }
            return response
        }

        private fun refreshAccessToken(): String? = runBlocking {
            val refreshToken = TokenManager.getInstance().getRefreshToken() ?: return@runBlocking null
            try {
                // Create a temporary API service without interceptor to avoid recursion
                val api = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
                val response = api.refreshToken("Bearer $refreshToken")
                // Save the new access token
                TokenManager.getInstance().saveToken(response.access_token)
                response.access_token
            } catch (e: Exception) {
                // Refresh failed – clear token and force logout
                TokenManager.getInstance().clear()
                null
            }
        }
    }
}
