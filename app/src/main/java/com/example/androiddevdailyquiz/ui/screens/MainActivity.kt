package com.example.androiddevdailyquiz.ui.screens

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.androiddevdailyquiz.data.model.QuestionType
import com.example.androiddevdailyquiz.ui.theme.DailyQuizTheme
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel
import com.example.androiddevdailyquiz.utils.DailyQuizWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
        setupDailyNotification()

        setContent {
            DailyQuizTheme {
                var currentScreen by remember { mutableStateOf("menu") }

                when (currentScreen) {
                    "menu" -> MainMenuScreen(
                        viewModel = quizViewModel,
                        onStartQuiz = { currentScreen = "quiz" },
                        onShowStats = { currentScreen = "stats" }
                    )

                    "quiz" -> {
                        BackHandler { currentScreen = "menu" }
                        QuizScreen(
                            viewModel = quizViewModel,
                            mode = QuestionType.MULTIPLE_CHOICE,
                            onBackToMenu = { currentScreen = "menu" }
                        )
                    }

                    "stats" -> {
                        BackHandler { currentScreen = "menu" }
                        StatisticsScreen(
                            viewModel = quizViewModel,
                            onBack = { currentScreen = "menu" }
                        )
                    }
                }
            }
        }
    }

    private fun setupDailyNotification() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val now = Calendar.getInstance()
        val targetHour = 11
        val initialDelay = calculateInitialDelay(now, targetHour)

        val dailyWork = PeriodicWorkRequestBuilder<DailyQuizWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_quiz_work",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWork
        )
    }

    private fun calculateInitialDelay(now: Calendar, targetHour: Int): Long {
        val target = now.clone() as Calendar
        target.set(Calendar.HOUR_OF_DAY, targetHour)
        target.set(Calendar.MINUTE, 0)
        target.set(Calendar.SECOND, 0)
        target.set(Calendar.MILLISECOND, 0)

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_MONTH, 1)
        }

        return target.timeInMillis - now.timeInMillis
    }
}