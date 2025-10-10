package com.example.androiddevdailyquiz.ui.screens

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
import com.example.androiddevdailyquiz.data.model.Question


@Composable
fun MultipleChoiceQuestion(
    question: Question,
    showAnswer: Boolean,
    onToggleAnswer: () -> Unit
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = question.question,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(24.dp))

        question.options?.forEach { option ->
            Button(
                onClick = {
                    selectedOption = option
                    onToggleAnswer()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        selectedOption == option && option == question.answer -> MaterialTheme.colorScheme.primary
                        selectedOption == option -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.secondary
                    }
                )
            ) {
                Text(option, color = MaterialTheme.colorScheme.onSecondary)
            }
        }

        if (showAnswer) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Correct Answer: ${question.answer}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}