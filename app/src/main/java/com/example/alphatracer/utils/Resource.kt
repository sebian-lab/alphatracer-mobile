package com.example.alphatracer.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Sealed class representing the result of an operation.
 */
sealed class Resource<T> {
    data class Loading<T>(val message: String? = null) : Resource<T>()
    data class Success<T>(val data: T, val message: String? = null) : Resource<T>()
    data class Error(val error: String?, val message: String? = null) : Resource<T>()
}

/**
 * Extension function to observe as StateFlow.
 */
fun <T> Resource<T>.asStateFlow(): StateFlow<Resource<T>> {
    return MutableStateFlow(this)
}
