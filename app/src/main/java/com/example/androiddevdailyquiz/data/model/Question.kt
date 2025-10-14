package com.example.androiddevdailyquiz.data.model

data class Question(
    val id: Int,
    val question: String,
    val answer: String,
    val type: QuestionType,
    val fakeOptions: List<String>? = null,
    val category: QuestionCategory = QuestionCategory.OTHER
)