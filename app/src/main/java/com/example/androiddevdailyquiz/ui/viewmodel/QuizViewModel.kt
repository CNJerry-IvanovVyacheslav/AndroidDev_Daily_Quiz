package com.example.androiddevdailyquiz.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androiddevdailyquiz.data.DataStoreManager
import com.example.androiddevdailyquiz.data.model.Question
import com.example.androiddevdailyquiz.data.repo.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuestionRepository(application)
    private val dataStore = DataStoreManager(application)

    private val _questions = MutableLiveData<List<Question>>(emptyList())
    val questions: LiveData<List<Question>> get() = _questions

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> get() = _currentIndex

    private val _currentQuestion = MutableLiveData<Question?>()
    val currentQuestion: LiveData<Question?> get() = _currentQuestion

    private val _showAnswer = MutableLiveData(false)
    val showAnswer: LiveData<Boolean> get() = _showAnswer

    // Статистика и стрик
    private val _correctAnswers = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswers

    private val _incorrectAnswers = MutableStateFlow(0)
    val incorrectAnswers: StateFlow<Int> = _incorrectAnswers

    private val _streakCount = MutableStateFlow(0)
    val streakCount: StateFlow<Int> = _streakCount

    private val _streakActive = MutableStateFlow(false)
    val streakActive: StateFlow<Boolean> = _streakActive

    init {
        loadQuestions()
        // Подгружаем сохранённые данные из DataStore
        viewModelScope.launch {
            dataStore.correctFlow.collectLatest { _correctAnswers.value = it }
        }
        viewModelScope.launch {
            dataStore.incorrectFlow.collectLatest { _incorrectAnswers.value = it }
        }
        viewModelScope.launch {
            dataStore.streakFlow.collectLatest { _streakCount.value = it }
        }
        viewModelScope.launch {
            dataStore.lastStreakDateFlow.collectLatest { lastDate ->
                val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    .format(java.util.Calendar.getInstance().time)
                _streakActive.value = (lastDate == today)
            }
        }
    }

    fun loadQuestions() {
        val loaded = repository.loadQuestions().shuffled()
        _questions.value = loaded
        if (loaded.isNotEmpty()) _currentQuestion.value = loaded[0]
    }

    fun nextQuestion() {
        val list = _questions.value ?: return
        val current = _currentIndex.value ?: 0
        val next = (current + 1) % list.size
        _currentIndex.value = next
        _currentQuestion.value = list[next]
        _showAnswer.value = false
    }

    fun toggleAnswer() {
        _showAnswer.value = !(_showAnswer.value ?: false)
    }

    fun checkAnswer(userInput: String): Boolean {
        val correct = _currentQuestion.value?.answer ?: return false
        val isCorrect = userInput.trim().equals(correct.trim(), ignoreCase = true)

        viewModelScope.launch {
            if (isCorrect) dataStore.incrementCorrect()
            else dataStore.incrementIncorrect()
            dataStore.updateStreak()
        }

        return isCorrect
    }

    fun getAccuracy(): Float {
        val total = _correctAnswers.value + _incorrectAnswers.value
        return if (total == 0) 0f else (_correctAnswers.value * 100f / total)
    }
}
