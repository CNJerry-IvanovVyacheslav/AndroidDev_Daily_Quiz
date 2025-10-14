package com.example.androiddevdailyquiz.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.androiddevdailyquiz.data.DataStoreManager
import kotlinx.coroutines.flow.first

class DailyQuizWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @SuppressLint("SimpleDateFormat")
    override suspend fun doWork(): Result {
        Log.d("DailyQuizWorker", "Worker triggered!")
        val dataStore = DataStoreManager(context)

        val lastDate = dataStore.lastStreakDateFlow.first()

        val today =
            java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Calendar.getInstance().time)

        if (lastDate != today) {
            NotificationHelper.showNotification(context)
        }

        return Result.success()
    }
}