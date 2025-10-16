package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.androiddevdailyquiz.data.model.Question


@Composable
fun OptionSelection(
    option: String,
    isSelected: Boolean,
    isCorrectAnswer: Boolean,
    isAnswerRevealed: Boolean,
    onClick: () -> Unit
) {
    val containerColor = when {
        // Option selected by user
        isSelected && isAnswerRevealed && isCorrectAnswer -> MaterialTheme.colorScheme.primary // Correct choice
        isSelected && isAnswerRevealed && !isCorrectAnswer -> MaterialTheme.colorScheme.errorContainer // Incorrect choice

        // Right answer when user has chosen incorrectly
        !isSelected && isAnswerRevealed && isCorrectAnswer ->
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)

        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = when {
        isSelected && isAnswerRevealed && isCorrectAnswer -> MaterialTheme.colorScheme.onPrimary
        isSelected && isAnswerRevealed && !isCorrectAnswer -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        tonalElevation = if (isSelected && isAnswerRevealed) 3.dp else 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(enabled = !isAnswerRevealed) { onClick() }
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
                color = textColor
            )
        }
    }
}

@Composable
fun BlankWithOptionsQuestion(
    question: Question,
    onCheckAnswer: (String) -> Boolean
) {
    var selectedOption by remember(question.id) { mutableStateOf<String?>(null) }
    var isCorrectAnswer by remember(question.id) { mutableStateOf<Boolean?>(null) }
    val isAnswerRevealed = selectedOption != null

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
        QuestionHeader(question)

        // Standard question text
        Text(
            text = question.question,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(24.dp))

        options.forEach { option ->
            OptionSelection(
                option = option,
                isSelected = selectedOption == option,
                isCorrectAnswer = option.trim().equals(question.answer.trim(), ignoreCase = true),
                isAnswerRevealed = isAnswerRevealed,
                onClick = {
                    selectedOption = option
                    isCorrectAnswer = onCheckAnswer(option)
                }
            )
        }
    }
}

// Common header for category display
@Composable
fun QuestionHeader(question: Question) {
    Text(
        text = "#${question.category.displayName}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        // Modifier.align(Alignment.Start) удален, чтобы избежать ошибки "Unresolved reference 'align'".
        // В Column это выравнивание происходит по умолчанию.
    )
    Spacer(Modifier.height(8.dp))
}

// Helper to extract the code block from the question string (assuming triple backticks)
fun extractCodeSnippet(question: String): Pair<String, String> {
    val regex = Regex("```(\\w+)?\\s*\\n([\\s\\S]*?)\\n```\\s*\\n?([\\s\\S]*)")
    val match = regex.find(question)

    return if (match != null) {
        val code = match.groups[2]?.value?.trim() ?: ""
        val remainingText = (match.groups[3]?.value?.trim() ?: "")
            .replaceFirst(Regex("^\\s*\\n"), "") // Remove leading newline/spaces after code block
        Pair(code, remainingText)
    } else {
        // If no code block, return the whole text as remaining text
        Pair("", question)
    }
}

@Composable
fun CodeOutputQuestion(
    question: Question,
    onCheckAnswer: (String) -> Boolean
) {
    var selectedOption by remember(question.id) { mutableStateOf<String?>(null) }
    var isCorrectAnswer by remember(question.id) { mutableStateOf<Boolean?>(null) }
    val isAnswerRevealed = selectedOption != null

    val options = remember(question.id) {
        val all = mutableListOf(question.answer)
        all.addAll(question.fakeOptions ?: emptyList())
        all.distinct().shuffled()
    }

    val (codeSnippet, prompt) = remember(question.id) {
        extractCodeSnippet(question.question)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        QuestionHeader(question)

        Text(
            text = "A practical question: What will be displayed in the console?",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(16.dp))

        // Code Snippet Display Area (Simulation of code styling)
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = codeSnippet,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        if (prompt.isNotEmpty()) {
            Text(
                text = prompt,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(16.dp))
        }

        options.forEach { option ->
            OptionSelection(
                option = option,
                isSelected = selectedOption == option,
                isCorrectAnswer = option.trim().equals(question.answer.trim(), ignoreCase = true),
                isAnswerRevealed = isAnswerRevealed,
                onClick = {
                    selectedOption = option
                    isCorrectAnswer = onCheckAnswer(option)
                }
            )
        }
    }
}
