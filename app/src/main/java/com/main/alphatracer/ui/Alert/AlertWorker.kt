package com.main.alphatracer.ui.Alert

import android.Manifest
import android.R
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.main.alphatracer.model.CandleResponse
import com.main.alphatracer.network.RetrofitClient
import com.main.alphatracer.ui.Alert.Data.AlertDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class AlertWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val dataStore = AlertDataStore(applicationContext)
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val rules = dataStore.getRules()
        val now = System.currentTimeMillis()
        val cooldownMs = 3600_000L

        for (rule in rules) {

            if (now - rule.lastTriggeredAt < cooldownMs) continue

            if (!rule.isActive()) continue

            try {
                val candles = fetchCandlesForRange(rule.ticker, "1m" ,rule.startDate, rule.endDate)
                if (candles.size < 2) continue

                val maxPrice = candles.maxOf { it.close }
                val currentPrice = candles.last().close
                val dropFromMax = ((maxPrice - currentPrice) / maxPrice) * 100

                if (dropFromMax >= rule.thresholdPercent) {

                    showNotification(
                        ticker = rule.ticker,
                        dropPercent = dropFromMax,
                        startDate = LocalDate.now(),
                        endDate = rule.endDate
                    )

                    val updatedRule = rule.copy(lastTriggeredAt = now)
                    val updatedRules = rules.map { if (it.id == rule.id) updatedRule else it }
                    dataStore.saveRules(updatedRules)
                }
            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
        Result.success()
    }


    private suspend fun fetchCandlesForRange(
        ticker: String,
        interval: String,
        start: LocalDate,
        end: LocalDate
    ): List<CandleResponse> {
        return try {

            RetrofitClient.apiService.getCandlesByDateRange(
                ticker = ticker,
                interval = interval,
                startDate = start.format(dateFormatter),
                endDate = end.format(dateFormatter)
            )
        } catch (e: Exception) {
            emptyList()
        }
    }



    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(ticker: String, dropPercent: Double, startDate: LocalDate, endDate: LocalDate) {

        val channelId = "alert_channel"
        val notificationId = ticker.hashCode()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Stock Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Triggered when your stock drops by a set percentage"
            }
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_dialog_alert)
            .setContentTitle("📉 Alert: $ticker dropped!")
            .setContentText(String.format("Down %.1f%% between %s and %s", dropPercent, startDate, endDate))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(notificationId, notification)
    }
}

fun scheduleStockWorker(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (context is Activity) {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
            return
        }
    }
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val periodicRequest = PeriodicWorkRequestBuilder<AlertWorker>(15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()


    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "stock_alert_worker",
        ExistingPeriodicWorkPolicy.KEEP,
        periodicRequest
    )
    sendTestAlert(context)
}
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
private fun sendTestAlert(context: Context) {
    val channelId = "alert_channel"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Stock Alerts",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_dialog_info)  // drawable to be implemented
        .setContentTitle("Alert System Active")
        .setContentText("Worker scheduled successfully.")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    NotificationManagerCompat.from(context).notify(999, notification)
}