package com.example.alphatracer.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import com.example.alphatracer.data.model.response.AuthResponse
import com.example.alphatracer.data.model.response.UpdateProfileRequest
import com.example.alphatracer.data.model.response.UserProfile

/**
 * API interface for authentication operations.
 */
interface AuthApi {
    
    /**
     * Register a new user.
     */
    @POST("/api/v1/auth/register")
    suspend fun register(
        @Body request: com.example.alphatracer.data.model.request.RegisterRequest
    ): Response<AuthResponse>
    
    /**
     * Login with email and password.
     */
    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body request: com.example.alphatracer.data.model.request.LoginRequest
    ): Response<AuthResponse>
    
    /**
     * Get current user profile.
     */
    @GET("/api/v1/users/me")
    suspend fun getProfile(
        @Header(Constants.AUTHORIZATION_HEADER) token: String
    ): Response<UserProfile>
    
    /**
     * Update current user profile.
     */
    @PUT("/api/v1/users/me")
    suspend fun updateProfile(
        @Header(Constants.AUTHORIZATION_HEADER) token: String,
        @Body request: UpdateProfileRequest
    ): Response<UserProfile>
}
