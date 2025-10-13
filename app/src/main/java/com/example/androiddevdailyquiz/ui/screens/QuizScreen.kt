package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androiddevdailyquiz.data.model.QuestionType
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    val navBarHeight = 72.dp

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = navBarHeight)
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                if (currentQuestion != null) {
                    BlankWithOptionsQuestion(
                        question = currentQuestion,
                        onCheckAnswer = { answer ->
                            hasAnswered = true
                            viewModel.checkAnswer(answer)
                        }
                    )
                } else {
                    Text(
                        text = "No questions available.",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .heightIn(min = navBarHeight),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
        }
    }
}
