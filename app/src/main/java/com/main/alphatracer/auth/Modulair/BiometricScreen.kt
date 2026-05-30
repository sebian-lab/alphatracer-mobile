package com.main.alphatracer.auth.Modulair

import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity



object BiometricAuthenticator {
    fun authenticate(
        activity: FragmentActivity,
        title: String = "Biometric Login",
        onSuccess: () -> Unit,
        onFailure: () -> Unit = {}
    ) {
        val executor = activity.mainExecutor
        val prompt =
            BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Log.w("Biometric", "Error $errorCode: $errString")
                    onFailure()
                }

                override fun onAuthenticationFailed() {
                    Log.w("Biometric", "Authentication failed")
                    onFailure()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle("Verify your identity")

            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            // Note: .setNegativeButtonText() cannot be used if DEVICE_CREDENTIAL is enabled
            .build()
        prompt.authenticate(promptInfo)
    }
}