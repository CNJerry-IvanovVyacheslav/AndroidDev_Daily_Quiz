package com.example.androiddevdailyquiz.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColors = darkColorScheme(
    primary = Color(0xFF4CAF50),        // зелёный для успеха
    onPrimary = Color.White,
    secondary = Color(0xFF2196F3),      // синий для кнопок
    onSecondary = Color.White,
    background = Color(0xFF121212),     // тёмный фон
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),        // тёмная поверхность для карточек
    onSurface = Color.White,
    error = Color(0xFFF44336),          // красный для ошибок
    onError = Color.White
)

@Composable
fun DailyQuizTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography,
        content = content
    )
}