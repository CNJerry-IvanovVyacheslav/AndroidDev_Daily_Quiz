package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androiddevdailyquiz.data.model.QuestionType
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onBackToMenu: () -> Unit
) {
    val currentQuestion by viewModel.currentQuestion.observeAsState()
    var hasAnswered by remember(currentQuestion?.id) { mutableStateOf(false) }

    // Logic to reset hasAnswered state when question changes
    currentQuestion?.id?.let {
        // If question ID changes, reset hasAnswered
        if (remember { mutableStateOf(it) }.value != it) {
            hasAnswered = false
            remember { mutableStateOf(it) }.value = it
        }
    }

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
                currentQuestion?.let { question ->
                    // NEW LOGIC: Select Composable based on QuestionType
                    val onCheckAnswer: (String) -> Boolean = { answer ->
                        hasAnswered = true
                        val isCorrect = viewModel.checkAnswerSync(answer)
                        viewModel.recordAnswerResult(isCorrect)
                        isCorrect
                    }

                    when (question.type) {
                        QuestionType.MULTIPLE_CHOICE -> BlankWithOptionsQuestion(
                            question = question,
                            onCheckAnswer = onCheckAnswer
                        )

                        QuestionType.CODE_SNIPPET_OUTPUT -> CodeOutputQuestion(
                            question = question,
                            onCheckAnswer = onCheckAnswer
                        )
                    }

                } ?: run {
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
                        hasAnswered = false // Reset state for the next question
                        viewModel.nextQuestion()
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
