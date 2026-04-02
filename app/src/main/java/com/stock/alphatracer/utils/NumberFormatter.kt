package com.stock.alphatracer.utils

import java.text.NumberFormat
import java.util.*

/**
 * Utility class for formatting numbers, currency, and percentages
 */
object NumberFormatter {
    
    /**
     * Format a number as currency (e.g., $1,234.56)
     */
    fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        return formatter.format(amount)
    }
    
    /**
     * Format a number as currency with compact notation (e.g., 1.2K)
     */
    fun formatCompactCurrency(amount: Double): String {
        val scaled = when {
            amount >= 1_000_000 -> { amount / 1_000_000.format("#.##") + "M" }
            amount >= 1_000 -> { amount / 1_000.format("#.##") + "K" }
            else -> formatCurrency(amount)
        }
        return scaled
    }
    
    /**
     * Format a percentage change (e.g., +12.34% or -5.67%)
     */
    fun formatPercentage(value: Double): String {
        val formatter = NumberFormat.getPercentInstance(Locale.US)
        formatter.maximumFractionDigits = 2
        return if (value >= 0) { "+${formatter.format(value)}" } else { "${formatter.format(value)}" }
    }
    
    /**
     * Format a number with decimals and optional sign prefix
     */
    fun formatNumberWithPrefix(value: Double, includeSign: Boolean = false): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.maximumFractionDigits = 2
        return if (includeSign && value != 0.0) {
            "${if (value >= 0) "+" else "-"}${formatter.format(value.abs())}"
        } else {
            formatter.format(value)
        }
    }
}
