package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androiddevdailyquiz.data.model.Question

@Composable
fun BlankWithOptionsQuestion(
    question: Question,
    onCheckAnswer: (String) -> Boolean
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<String?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

    val options = remember {
        val all = mutableListOf(question.answer)
        all.addAll(question.fakeOptions ?: emptyList())
        all.shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = question.question.replace("___", "_____"),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(24.dp))

        options.forEach { option ->
            Button(
                onClick = {
                    selectedOption = option
                    val correct = onCheckAnswer(option)
                    isCorrect = correct
                    result = if (correct) "✅ Correct!" else "❌ Incorrect! Correct: ${question.answer}"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        selectedOption == option && isCorrect == true -> MaterialTheme.colorScheme.primaryContainer
                        selectedOption == option && isCorrect == false -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.secondaryContainer
                    }
                )
            ) {
                Text(
                    text = option,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        result?.let {
            Spacer(Modifier.height(24.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                color = when (isCorrect) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onBackground
                }
            )
        }
    }
}