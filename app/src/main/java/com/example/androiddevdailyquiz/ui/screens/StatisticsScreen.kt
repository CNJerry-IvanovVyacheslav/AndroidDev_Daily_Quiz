package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: QuizViewModel, onBack: () -> Unit) {
    val correct by viewModel.correctAnswers.collectAsState()
    val incorrect by viewModel.incorrectAnswers.collectAsState()
    val streak by viewModel.streakCount.collectAsState()
    val streakActive by viewModel.streakActive.collectAsState()
    val maxConsecutive by viewModel.maxConsecutive.collectAsState()
    val accuracy = viewModel.getAccuracy()

    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Your Progress",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StatItem(title = "Total Questions Answered", value = (correct + incorrect).toString())
            StatItem(title = "Correct Answers", value = correct.toString())
            StatItem(title = "Incorrect Answers", value = incorrect.toString())

            Spacer(Modifier.height(8.dp))

            StatItem(
                title = "Accuracy Rate",
                value = "${"%.1f".format(accuracy)}%",
                description = when {
                    accuracy == 0f -> "Everyone starts somewhere — try your first quiz! 💡"
                    accuracy < 50f -> "Don’t worry, mistakes are part of learning 👣"
                    accuracy < 75f -> "Good progress — keep refining your skills 💪"
                    accuracy < 90f -> "Strong performance! You’re becoming consistent 🔥"
                    else -> "Outstanding accuracy — true Android pro ⚡"
                }
            )

            Divider()
            Spacer(Modifier.height(8.dp))

            StatItem(
                title = "Longest Correct Streak",
                value = maxConsecutive.toString(),
                description = when {
                    maxConsecutive == 0 -> "Keep going — your best streak is ahead 🚀"
                    maxConsecutive < 5 -> "Nice start! Aim for 5+ in a row 💪"
                    maxConsecutive < 10 -> "You're improving — great momentum 🔥"
                    else -> "Incredible focus! You're unstoppable ⚡"
                }
            )

            Spacer(Modifier.height(8.dp))

            StatItem(
                title = "Active Daily Streak (days)",
                value = streak.toString(),
                description = if (streakActive)
                    "You’re on fire today 🔥"
                else
                    "No quiz yet today 💡"
            )

            Spacer(Modifier.height(36.dp))

            Button(
                onClick = { showResetDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Reset Statistics", color = MaterialTheme.colorScheme.onError)
            }

            Spacer(Modifier.height(16.dp))
        }

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Reset Statistics") },
                text = { Text("Are you sure you want to reset all your progress, including your best streak?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.resetStatistics()
                            showResetDialog = false
                        }
                    ) { Text("Yes, reset") }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun StatItem(title: String, value: String, description: String? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        if (description != null) {
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .alpha(0.7f)
            )
        }
    }
}

