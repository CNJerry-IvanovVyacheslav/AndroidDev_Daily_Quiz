package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.androiddevdailyquiz.R
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel

@Composable
fun MainMenuScreen(
    viewModel: QuizViewModel,
    onStartQuiz: () -> Unit,
    onShowStats: () -> Unit
) {
    val streakCount by viewModel.streakCount.collectAsState()
    val streakActive by viewModel.streakActive.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(
                        id = if (streakActive) R.drawable.ic_fire_active else R.drawable.ic_fire_inactive
                    ),
                    contentDescription = "Streak Icon",
                    modifier = Modifier.size(28.dp),
                    colorFilter = if (!streakActive)
                        ColorFilter.tint(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                    else null
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = streakCount.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (streakActive)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }

            Text(
                text = "Android Dev Daily Quiz",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = onStartQuiz,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Start Quiz", color = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onShowStats,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Statistics", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}
