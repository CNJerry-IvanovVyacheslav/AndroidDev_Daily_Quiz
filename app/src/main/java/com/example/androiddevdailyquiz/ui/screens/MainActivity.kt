package com.example.androiddevdailyquiz.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androiddevdailyquiz.data.model.QuestionType
import com.example.androiddevdailyquiz.ui.theme.DailyQuizTheme
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel


class MainActivity : ComponentActivity() {
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DailyQuizTheme {
                ModeSelectionScreen(viewModel = quizViewModel)
            }
        }
    }

    @Composable
    fun ModeSelectionScreen(viewModel: QuizViewModel) {
        var selectedMode by remember { mutableStateOf<QuestionType?>(null) }

        BackHandler(enabled = selectedMode != null) {
            selectedMode = null
        }

        if (selectedMode == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Choose Mode",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = { selectedMode = QuestionType.MULTIPLE_CHOICE },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Quiz (Multiple Choice)", color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { selectedMode = QuestionType.SELF_PRACTICE },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Practice (Fill in the blank)", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        } else {
            QuizScreen(viewModel = viewModel, mode = selectedMode!!)
        }
    }
}
