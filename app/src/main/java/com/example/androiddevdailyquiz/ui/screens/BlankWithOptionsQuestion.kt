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
    key(question.id) {
        var selectedOption by remember { mutableStateOf<String?>(null) }
        var isCorrect by remember { mutableStateOf<Boolean?>(null) }

        val options = remember(question.id) {
            val all = mutableListOf(question.answer)
            all.addAll(question.fakeOptions ?: emptyList())
            all.distinct().shuffled()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(
                text = question.question.replace("___", "_____"),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(24.dp))

            options.forEach { option ->
                val isSelected = selectedOption == option
                val isAnswerCorrect = question.answer.trim().equals(option.trim(), ignoreCase = true)

                val buttonColor = when {
                    isSelected && isCorrect == true -> MaterialTheme.colorScheme.primary
                    isSelected && isCorrect == false -> MaterialTheme.colorScheme.errorContainer
                    selectedOption != null && isCorrect == false && isAnswerCorrect ->
                        MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.secondaryContainer
                }

                Button(
                    onClick = {
                        if (selectedOption == null) {
                            selectedOption = option
                            isCorrect = onCheckAnswer(option)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    enabled = selectedOption == null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        disabledContainerColor = buttonColor
                    )
                ) {
                    Text(
                        text = option,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}