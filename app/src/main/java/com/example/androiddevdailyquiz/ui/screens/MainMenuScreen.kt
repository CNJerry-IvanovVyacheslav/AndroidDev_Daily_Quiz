package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.androiddevdailyquiz.R
import com.example.androiddevdailyquiz.data.model.QuestionCategory
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel


@Composable
fun MainMenuScreen(
    viewModel: QuizViewModel,
    onStartQuiz: () -> Unit,
    onShowStats: () -> Unit
) {
    val streakCount by viewModel.streakCount.collectAsState()
    val streakActive by viewModel.streakActive.collectAsState()
    val selectedCategory by viewModel.selectedCategory.observeAsState(QuestionCategory.ALL)

    val allQuestions = viewModel.allQuestions

    val questionCounts = remember(allQuestions) {
        val counts = allQuestions.groupingBy { it.category }.eachCount().toMutableMap()
        counts[QuestionCategory.ALL] = counts.filterKeys { it != QuestionCategory.ALL }.values.sum()
        counts.withDefault { 0 }
    }

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.7f)
                ) {
                    CategorySelector(
                        categories = QuestionCategory.values().toList(),
                        questionCounts = questionCounts,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.setCategory(it) },
                    )
                }

                Spacer(Modifier.width(16.dp))

                Row(
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
            }

            Text(
                text = "AndroidDev Daily Quiz",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
            )

            Button(
                onClick = onStartQuiz,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = selectedCategory != null
            ) {
                Text(
                    "Start Quiz",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onShowStats,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    "Statistics",
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}