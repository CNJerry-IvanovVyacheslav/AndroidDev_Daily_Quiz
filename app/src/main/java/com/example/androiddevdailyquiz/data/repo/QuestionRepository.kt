package com.example.androiddevdailyquiz.data.repo

import android.content.Context
import com.example.androiddevdailyquiz.data.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuestionRepository(private val context: Context) {

    private val gson = Gson()
    private val type = object : TypeToken<List<Question>>() {}.type

    fun loadQuestions(): List<Question> {
        return try {
            val inputStream = context.assets.open("questions.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}