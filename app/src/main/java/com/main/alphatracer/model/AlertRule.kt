package com.main.alphatracer.model
import java.time.LocalDate
import java.util.UUID

data class AlertRule(
    val id: String = UUID.randomUUID().toString(),
    val ticker: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val thresholdPercent: Double,
    var lastTriggeredAt: Long = 0L
) {

    fun isActive(): Boolean = !LocalDate.now().isAfter(endDate)
}