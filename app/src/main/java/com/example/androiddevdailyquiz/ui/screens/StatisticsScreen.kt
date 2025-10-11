package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel

@Composable
fun StatisticsScreen(viewModel: QuizViewModel, onBack: () -> Unit) {
    val correct by viewModel.correctAnswers.collectAsState()
    val incorrect by viewModel.incorrectAnswers.collectAsState()
    val streak by viewModel.streakCount.collectAsState()
    val streakActive by viewModel.streakActive.collectAsState()
    val accuracy = viewModel.getAccuracy()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = "Your Statistics",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(16.dp))

        Text("Correct Answers: $correct")
        Text("Incorrect Answers: $incorrect")
        Text("Accuracy: ${"%.1f".format(accuracy)}%")
        Spacer(Modifier.height(16.dp))
        Text("Current Streak: $streak")
        Text("Streak Active: ${if (streakActive) "Yes" else "No"}")

        Spacer(Modifier.height(32.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Menu")
        }
    }
}
