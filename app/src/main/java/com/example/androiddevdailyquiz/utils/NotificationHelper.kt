package com.example.androiddevdailyquiz.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.androiddevdailyquiz.R

object NotificationHelper {

    private const val CHANNEL_ID = "daily_quiz_channel"
    private const val CHANNEL_NAME = "Daily Quiz"
    private const val CHANNEL_DESC = "Напоминания о ежедневном квизе"

    fun showNotification(context: Context) {
        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // заменишь на свой
            .setContentTitle("AndroidDev Daily Quiz")
            .setContentText("Пора решить ежедневный вопрос!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESC
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}