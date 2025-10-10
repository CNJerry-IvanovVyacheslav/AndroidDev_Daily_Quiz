package com.example.androiddevdailyquiz.data.model

data class Question(
    val id: Int,
    val type: QuestionType,
    val question: String,
    val options: List<String>? = null,
    val answer: String
)