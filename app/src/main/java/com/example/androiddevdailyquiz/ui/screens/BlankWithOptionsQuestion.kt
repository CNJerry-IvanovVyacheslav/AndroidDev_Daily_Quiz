package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

        val options = remember(question.id) {
            val all = mutableListOf(question.answer)
            all.addAll(question.fakeOptions ?: emptyList())
            all.distinct().shuffled()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "#${question.category.displayName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.Start)
            )

            Text(
                text = question.question,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(24.dp))

            options.forEach { option ->
                val isSelected = selectedOption == option
                val isAnswerCorrect =
                    option.trim().equals(question.answer.trim(), ignoreCase = true)

                val containerColor = when {
                    isSelected && isAnswerCorrect -> MaterialTheme.colorScheme.primary
                    isSelected && !isAnswerCorrect -> MaterialTheme.colorScheme.errorContainer
                    selectedOption != null && isAnswerCorrect -> MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.5f
                    )

                    else -> MaterialTheme.colorScheme.surfaceVariant
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = containerColor,
                    tonalElevation = if (isSelected) 3.dp else 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable(enabled = selectedOption == null) {
                            selectedOption = option
                            onCheckAnswer(option)
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}