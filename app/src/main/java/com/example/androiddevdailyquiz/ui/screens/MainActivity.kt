package com.example.androiddevdailyquiz.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.androiddevdailyquiz.data.model.QuestionType
import com.example.androiddevdailyquiz.ui.theme.DailyQuizTheme
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}