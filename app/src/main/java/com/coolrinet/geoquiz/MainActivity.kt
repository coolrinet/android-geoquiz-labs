package com.coolrinet.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.coolrinet.geoquiz.databinding.ActivityMainBinding
import kotlin.math.round

private const val TAG = "MainActivity"

private const val QUESTION_INDEX = "question_index"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0
    private var rightQuestionCount = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            questionBank[currentIndex].isAnswered = true
            toggleAnswerButtons()
            if (countAnsweredQuestions() == questionBank.size) {
                binding.nextButton.isVisible = false
                showResult()
            }
        }

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            questionBank[currentIndex].isAnswered = true
            toggleAnswerButtons()
            if (countAnsweredQuestions() == questionBank.size) {
                binding.nextButton.isVisible = false
                showResult()
            }
        }

        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            toggleAnswerButtons()
        }

        binding.prevButton.setOnClickListener {
            currentIndex = (currentIndex - 1) % questionBank.size
            updateQuestion()
            toggleAnswerButtons()
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(QUESTION_INDEX, currentIndex)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentIndex = savedInstanceState.getInt(QUESTION_INDEX)
        updateQuestion()
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId: Int
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            rightQuestionCount += 1
        } else {
            messageResId = R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }

    private fun toggleAnswerButtons() {
        if (questionBank[currentIndex].isAnswered) {
            binding.trueButton.isVisible = false
            binding.falseButton.isVisible = false
        } else {
            binding.trueButton.isVisible = true
            binding.falseButton.isVisible = true
        }
    }

    private fun countAnsweredQuestions(): Int {
        return questionBank.count { it.isAnswered }
    }

    private fun showResult() {
        val percentage = round(rightQuestionCount / questionBank.size * 100).toInt()
        val resultText = getString(R.string.result_toast, percentage)
        Toast.makeText(
            this,
            resultText,
            Toast.LENGTH_SHORT
        ).show()
    }
}