package com.example.androiddevdailyquiz.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.androiddevdailyquiz.data.model.QuestionCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val Context.dataStore by preferencesDataStore("quiz_stats")


class DataStoreManager(private val context: Context) {

    companion object {
        private val CORRECT_KEY = intPreferencesKey("correct")
        private val INCORRECT_KEY = intPreferencesKey("incorrect")
        private val STREAK_COUNT_KEY = intPreferencesKey("streak_count")
        private val LAST_STREAK_DATE_KEY = stringPreferencesKey("last_streak_date")
        private val CURRENT_CONSECUTIVE_KEY = intPreferencesKey("current_consecutive")
        private val MAX_CONSECUTIVE_KEY = intPreferencesKey("max_consecutive")

        // Ключ для категорий
        fun categoryIncorrectKey(category: QuestionCategory) =
            intPreferencesKey("incorrect_${category.name.lowercase()}")
    }

    val correctFlow: Flow<Int> = context.dataStore.data.map { it[CORRECT_KEY] ?: 0 }
    val incorrectFlow: Flow<Int> = context.dataStore.data.map { it[INCORRECT_KEY] ?: 0 }
    val streakFlow: Flow<Int> = context.dataStore.data.map { it[STREAK_COUNT_KEY] ?: 0 }
    val lastStreakDateFlow: Flow<String> =
        context.dataStore.data.map { it[LAST_STREAK_DATE_KEY] ?: "" }

    val currentConsecutiveFlow: Flow<Int> =
        context.dataStore.data.map { it[CURRENT_CONSECUTIVE_KEY] ?: 0 }
    val maxConsecutiveFlow: Flow<Int> = context.dataStore.data.map { it[MAX_CONSECUTIVE_KEY] ?: 0 }

    private val allCategories = QuestionCategory.values()

    val incorrectByCategoryFlow: Flow<Map<QuestionCategory, Int>> =
        context.dataStore.data.map { prefs ->
            allCategories.associateWith { category ->
                prefs[categoryIncorrectKey(category)] ?: 0
            }
        }

    suspend fun incrementCorrect() = context.dataStore.edit { prefs ->
        prefs[CORRECT_KEY] = (prefs[CORRECT_KEY] ?: 0) + 1

        val curr = (prefs[CURRENT_CONSECUTIVE_KEY] ?: 0) + 1
        prefs[CURRENT_CONSECUTIVE_KEY] = curr

        val max = prefs[MAX_CONSECUTIVE_KEY] ?: 0
        if (curr > max) prefs[MAX_CONSECUTIVE_KEY] = curr
    }

    suspend fun incrementIncorrect(category: QuestionCategory) = context.dataStore.edit { prefs ->
        prefs[INCORRECT_KEY] = (prefs[INCORRECT_KEY] ?: 0) + 1
        prefs[CURRENT_CONSECUTIVE_KEY] = 0

        val curr = (prefs[categoryIncorrectKey(category)] ?: 0) + 1
        prefs[categoryIncorrectKey(category)] = curr
    }

    suspend fun updateStreak() = context.dataStore.edit { prefs ->
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Calendar.getInstance().time)

        val lastDate = prefs[LAST_STREAK_DATE_KEY] ?: ""
        if (lastDate != today) {
            prefs[STREAK_COUNT_KEY] = (prefs[STREAK_COUNT_KEY] ?: 0) + 1
            prefs[LAST_STREAK_DATE_KEY] = today
        }
    }

    suspend fun resetStats() = context.dataStore.edit { prefs ->
        prefs[CORRECT_KEY] = 0
        prefs[INCORRECT_KEY] = 0
        prefs[STREAK_COUNT_KEY] = 0
        prefs[LAST_STREAK_DATE_KEY] = ""
        prefs[CURRENT_CONSECUTIVE_KEY] = 0
        prefs[MAX_CONSECUTIVE_KEY] = 0

        allCategories.forEach { category ->
            prefs[categoryIncorrectKey(category)] = 0
        }
    }
}