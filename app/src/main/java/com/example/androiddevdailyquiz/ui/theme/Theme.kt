package com.example.androiddevdailyquiz.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColors = darkColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color.White,
    secondary = Color(0xFF2196F3),
    onSecondary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    error = Color(0xFFF44336),
    onError = Color.White
)

@Composable
fun DailyQuizTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography
    ) {
        // Surface задаёт базовый фон и цвет текста по умолчанию
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            content()
        }
    }
}