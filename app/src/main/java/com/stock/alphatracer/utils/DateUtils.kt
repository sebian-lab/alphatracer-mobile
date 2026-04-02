package com.stock.alphatracer.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility class for date and time formatting
 */
object DateUtils {
    
    private val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    private val timeFormatter = SimpleDateFormat("hh:mm a", Locale.US)
    private val dateTimeFormatter = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.US)
    
    /**
     * Format a Date object to "MMM dd, yyyy" (e.g., Jan 15, 2024)
     */
    fun formatDate(date: Date): String {
        return dateFormatter.format(date)
    }
    
    /**
     * Format a timestamp (Long) to "MMM dd, yyyy"
     */
    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        return formatDate(date)
    }
    
    /**
     * Format a Date object to "hh:mm a" (e.g., 03:45 PM)
     */
    fun formatTime(date: Date): String {
        return timeFormatter.format(date)
    }
    
    /**
     * Format a timestamp to "hh:mm a"
     */
    fun formatTimestampTime(timestamp: Long): String {
        val date = Date(timestamp)
        return formatTime(date)
    }
    
    /**
     * Format a Date object to full datetime "MMM dd, yyyy hh:mm a"
     */
    fun formatDateTime(date: Date): String {
        return dateTimeFormatter.format(date)
    }
    
    /**
     * Format a timestamp to full datetime
     */
    fun formatTimestampDateTime(timestamp: Long): String {
        val date = Date(timestamp)
        return formatDateTime(date)
    }
}
