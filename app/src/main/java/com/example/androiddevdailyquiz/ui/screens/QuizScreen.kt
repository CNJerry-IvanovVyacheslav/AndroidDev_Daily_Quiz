package com.example.androiddevdailyquiz.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.androiddevdailyquiz.data.model.QuestionType
import com.example.androiddevdailyquiz.ui.viewmodel.QuizViewModel

@Composable
fun QuizScreen(viewModel: QuizViewModel, mode: QuestionType) {
    val questions by viewModel.questions.observeAsState(emptyList())
    val currentIndex by viewModel.currentIndex.observeAsState(0)
    val currentQuestion = questions.getOrNull(currentIndex)
    val showAnswer by viewModel.showAnswer.observeAsState(false)

    currentQuestion?.let { question ->
        if (question.type != mode) {
            LaunchedEffect(key1 = question) {
                viewModel.nextQuestion()
            }
        } else {
            when (question.type) {
                QuestionType.MULTIPLE_CHOICE -> MultipleChoiceQuestion(
                    question = question,
                    showAnswer = showAnswer,
                    onToggleAnswer = { viewModel.toggleAnswer() }
                )
                QuestionType.SELF_PRACTICE -> FillInTheBlankQuestion(
                    question = question,
                    showAnswer = showAnswer,
                    onCheckAnswer = { userAnswer -> viewModel.checkFillInAnswer(userAnswer) }
                )
            }
        }
    }
}