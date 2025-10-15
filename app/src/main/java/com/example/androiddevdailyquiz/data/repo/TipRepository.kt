package com.example.androiddevdailyquiz.data.repo

import android.content.Context
import com.example.androiddevdailyquiz.data.model.Tip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class TipRepository(private val context: Context) {

    private val tips: List<Tip> by lazy {
        loadTipsFromJson()
    }

    private fun loadTipsFromJson(): List<Tip> {
        return try {
            val jsonString = context.assets.open("tips.json").bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Tip>>() {}.type
            Gson().fromJson(jsonString, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getTipOfTheDay(): String {
        if (tips.isEmpty()) {
            return "No tips available at the moment."
        }
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        return tips[dayOfYear % tips.size].tip
    }
}