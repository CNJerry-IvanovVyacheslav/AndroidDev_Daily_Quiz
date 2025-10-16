package com.example.androiddevdailyquiz.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androiddevdailyquiz.data.DataStoreManager
import com.example.androiddevdailyquiz.data.model.Question
import com.example.androiddevdailyquiz.data.model.QuestionCategory
import com.example.androiddevdailyquiz.data.repo.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuestionRepository(application)
    private val dataStore = DataStoreManager(application)

    private val _selectedCategory = MutableLiveData(QuestionCategory.ALL)
    val selectedCategory: LiveData<QuestionCategory> get() = _selectedCategory

    private val _maxErrorsCategory =
        MutableStateFlow<Pair<QuestionCategory, Int>>(QuestionCategory.OTHER to 0)
    val maxErrorsCategory: StateFlow<Pair<QuestionCategory, Int>> = _maxErrorsCategory

    private val _allQuestions = repository.loadQuestions()
    val allQuestions: List<Question> get() = _allQuestions

    private val _questions = MutableLiveData<List<Question>>(emptyList())
    val questions: LiveData<List<Question>> get() = _questions

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> get() = _currentIndex

    private val _currentQuestion = MutableLiveData<Question?>()
    val currentQuestion: LiveData<Question?> get() = _currentQuestion

    private val _showAnswer = MutableLiveData(false)
    val showAnswer: LiveData<Boolean> get() = _showAnswer

    private val _correctAnswers = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswers

    private val _incorrectAnswers = MutableStateFlow(0)
    val incorrectAnswers: StateFlow<Int> = _incorrectAnswers

    private val _streakCount = MutableStateFlow(0)
    val streakCount: StateFlow<Int> = _streakCount

    private val _accuracy = MutableStateFlow(0f)
    val accuracy: StateFlow<Float> = _accuracy

    private val _streakActive = MutableStateFlow(false)
    val streakActive: StateFlow<Boolean> = _streakActive

    private val _maxConsecutive = MutableStateFlow(0)
    val maxConsecutive: StateFlow<Int> = _maxConsecutive

    private val _tipOfTheDay = MutableStateFlow("")
    val tipOfTheDay: StateFlow<String> = _tipOfTheDay

    val isDataStoreLoaded = MutableStateFlow(false)

    init {
        loadQuestions()

        viewModelScope.launch {
            dataStore.initializeIfNeeded()
            dataStore.updateTipIfNeeded()

            dataStore.checkAndResetStreakIfMissed()

            launch {
                dataStore.correctFlow.collectLatest {
                    _correctAnswers.value = it; recalcAccuracy()
                }
            }
            launch {
                dataStore.incorrectFlow.collectLatest {
                    _incorrectAnswers.value = it; recalcAccuracy()
                }
            }
            launch { dataStore.streakFlow.collectLatest { _streakCount.value = it } }
            launch {
                dataStore.lastStreakDateFlow.collectLatest { lastDate ->
                    val today =
                        java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                            .format(java.util.Calendar.getInstance().time)
                    _streakActive.value = (lastDate == today)
                }
            }
            launch { dataStore.maxConsecutiveFlow.collectLatest { _maxConsecutive.value = it } }
            launch {
                dataStore.incorrectByCategoryFlow.collectLatest { map ->
                    val maxPair =
                        map.maxByOrNull { it.value }?.toPair() ?: (QuestionCategory.OTHER to 0)
                    _maxErrorsCategory.value = maxPair
                }
            }
            launch { dataStore.tipOfTheDayFlow.collectLatest { _tipOfTheDay.value = it } }

            isDataStoreLoaded.value = true
        }
    }

    fun setCategory(category: QuestionCategory?) {
        _selectedCategory.value = category ?: QuestionCategory.OTHER
        loadQuestions()
    }

    fun loadQuestions() {
        val filtered = _selectedCategory.value?.let { cat ->
            when (cat) {
                QuestionCategory.ALL -> _allQuestions
                QuestionCategory.OTHER -> _allQuestions.filter { it.category == QuestionCategory.OTHER }
                else -> _allQuestions.filter { it.category == cat }
            }
        } ?: _allQuestions

        val shuffled = filtered.shuffled()
        _questions.value = shuffled
        _currentIndex.value = 0
        _currentQuestion.value =
            shuffled.firstOrNull()
    }

    fun nextQuestion() {
        val list = _questions.value ?: return
        val current = _currentIndex.value ?: 0
        val next = (current + 1) % list.size
        _currentIndex.value = next
        _currentQuestion.value = list[next]
        _showAnswer.value = false
    }

    fun checkAnswerSync(userInput: String): Boolean {
        val currentQuestion = _currentQuestion.value ?: return false

        Log.d("QuizDebug", "--- Comparing responses ---")
        Log.d("QuizDebug", "User Input: '${userInput}' (Length: ${userInput.length})")
        Log.d(
            "QuizDebug",
            "Right answer:  '${currentQuestion.answer}' (Length: ${currentQuestion.answer.length})"
        )

        val trimmedUserInput = userInput.trim()
        val trimmedCorrectAnswer = currentQuestion.answer.trim()
        Log.d(
            "QuizDebug",
            "Cropped input:   '${trimmedUserInput}' (Length: ${trimmedUserInput.length})"
        )
        Log.d(
            "QuizDebug",
            "Cropped response:  '${trimmedCorrectAnswer}' (Length: ${trimmedCorrectAnswer.length})"
        )

        val isCorrect = trimmedUserInput.equals(trimmedCorrectAnswer, ignoreCase = true)
        Log.d("QuizDebug", "Comparison result: $isCorrect")
        Log.d("QuizDebug", "--------------------------")

        return isCorrect
    }

    fun recordAnswerResult(isCorrect: Boolean) {
        viewModelScope.launch {
            val currentQuestion = _currentQuestion.value ?: return@launch
            if (isCorrect) {
                dataStore.incrementCorrect()
            } else {
                dataStore.incrementIncorrect(currentQuestion.category)
            }
            dataStore.updateStreak()
        }
    }

    private fun recalcAccuracy() {
        val total = _correctAnswers.value + _incorrectAnswers.value
        _accuracy.value = if (total == 0) 0f else (_correctAnswers.value * 100f / total)
    }

    fun resetStatistics() {
        viewModelScope.launch {
            dataStore.resetStats()
            _correctAnswers.value = 0
            _incorrectAnswers.value = 0
            _maxErrorsCategory.value = QuestionCategory.OTHER to 0
            _accuracy.value = 0f
        }
    }
}