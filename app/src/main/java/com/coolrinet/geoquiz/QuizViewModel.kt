package com.coolrinet.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.math.round

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var isCheater: Boolean
        get() = savedStateHandle[IS_CHEATER_KEY] ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    private var currentIndex
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    var rightQuestionCount = 0f

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val totalQuestionCount: Int
        get() = questionBank.size

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        val potentialIndex = (currentIndex - 1) % questionBank.size
        currentIndex = if (potentialIndex < 0) {
            questionBank.size - 1
        } else {
            potentialIndex
        }
    }

    fun countAnsweredQuestions(): Int {
        return questionBank.count { it.isAnswered }
    }

    fun changeCurrentQuestionStatus(status: Boolean) {
        questionBank[currentIndex].isAnswered = status
    }

    fun checkCurrentQuestionStatus(): Boolean {
        return questionBank[currentIndex].isAnswered
    }

    fun calculateRightAnswersPercentage(): Int {
        return round(rightQuestionCount / questionBank.size * 100).toInt()
    }
}