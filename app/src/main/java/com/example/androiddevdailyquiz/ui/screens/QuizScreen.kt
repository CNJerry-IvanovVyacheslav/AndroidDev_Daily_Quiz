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
    val showAnswer by viewModel.showAnswer.observeAsState(false)

    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswered by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (currentQuestion != null) {
            Column {
                Text(
                    text = currentQuestion.question,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(24.dp))

                currentQuestion.fakeOptions?.forEach { option ->
                    Button(
                        onClick = {
                            selectedOption = option
                            isAnswered = true
                            viewModel.toggleAnswer()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                !isAnswered -> MaterialTheme.colorScheme.secondary
                                option == currentQuestion.answer -> MaterialTheme.colorScheme.primary
                                selectedOption == option -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.secondary
                            }
                        )
                    ) {
                        Text(option, color = MaterialTheme.colorScheme.onSecondary)
                    }
                }

                if (showAnswer && isAnswered) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "✅ Correct answer: ${currentQuestion.answer}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

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
                        selectedOption = null
                        isAnswered = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
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
