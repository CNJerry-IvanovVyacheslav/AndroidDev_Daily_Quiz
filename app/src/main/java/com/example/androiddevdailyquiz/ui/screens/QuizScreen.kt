package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androiddevdailyquiz.data.model.QuestionType
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    mode: QuestionType,
    onBackToMenu: () -> Unit
) {
    val questions by viewModel.questions.observeAsState(emptyList())
    val currentIndex by viewModel.currentIndex.observeAsState(0)
    val currentQuestion = questions.getOrNull(currentIndex)

    var hasAnswered by remember(currentQuestion?.id) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (currentQuestion != null) {
            BlankWithOptionsQuestion(
                question = currentQuestion,
                onCheckAnswer = { answer ->
                    hasAnswered = true
                    viewModel.checkAnswer(answer)
                }
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onBackToMenu,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Back to Menu", color = MaterialTheme.colorScheme.onSecondary)
                }

                Button(
                    onClick = {
                        viewModel.nextQuestion()
                        hasAnswered = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = hasAnswered
                ) {
                    Text("Next Question", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        } else {
            Text(
                text = "No questions available.",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
