package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel

@Composable
fun StatisticsScreen(viewModel: QuizViewModel, onBack: () -> Unit) {
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

        Text("Correct Answers: 12")
        Text("Incorrect Answers: 3")
        Text("Accuracy: 80%")

        Spacer(Modifier.height(32.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Menu")
        }
    }
}
