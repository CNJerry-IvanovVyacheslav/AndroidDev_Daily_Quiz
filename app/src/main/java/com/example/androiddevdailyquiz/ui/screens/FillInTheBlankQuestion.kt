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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
fun FillInTheBlankQuestion(
    question: Question,
    showAnswer: Boolean,
    onCheckAnswer: (String) -> Boolean
) {
    var userInput by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

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

        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Your answer", color = MaterialTheme.colorScheme.onBackground) },
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val correct = onCheckAnswer(userInput)
                isCorrect = correct
                result = if (correct) {
                    "✅ Correct!"
                } else {
                    "❌ Incorrect! Correct: ${question.answer}"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Check Answer", color = MaterialTheme.colorScheme.onPrimary)
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