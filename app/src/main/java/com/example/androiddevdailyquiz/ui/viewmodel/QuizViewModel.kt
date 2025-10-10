package com.example.androiddevdailyquiz.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androiddevdailyquiz.data.model.Question
import com.example.androiddevdailyquiz.data.repo.QuestionRepository

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuestionRepository(application)

    private val _questions = MutableLiveData<List<Question>>(emptyList())
    val questions: LiveData<List<Question>> get() = _questions

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> get() = _currentIndex

    private val _currentQuestion = MutableLiveData<Question?>()
    val currentQuestion: LiveData<Question?> get() = _currentQuestion

    private val _showAnswer = MutableLiveData(false)
    val showAnswer: LiveData<Boolean> get() = _showAnswer

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        val loaded = repository.loadQuestions()
        _questions.value = loaded
        if (loaded.isNotEmpty()) {
            _currentQuestion.value = loaded[0]
        }
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

    fun checkFillInAnswer(userInput: String): Boolean {
        val correct = _currentQuestion.value?.answer ?: return false
        return userInput.trim().equals(correct.trim(), ignoreCase = true)
    }
}