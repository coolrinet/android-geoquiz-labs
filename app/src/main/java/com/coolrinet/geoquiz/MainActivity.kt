package com.coolrinet.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.coolrinet.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener {
            checkAnswer(true)
            quizViewModel.changeCurrentQuestionStatus(true)
            toggleAnswerButtons()
            if (quizViewModel.countAnsweredQuestions() == quizViewModel.totalQuestionCount) {
                binding.nextButton.isVisible = false
                binding.prevButton.isVisible = false
                showResult()
            }
        }

        binding.falseButton.setOnClickListener {
            checkAnswer(false)
            quizViewModel.changeCurrentQuestionStatus(true)
            toggleAnswerButtons()
            if (quizViewModel.countAnsweredQuestions() == quizViewModel.totalQuestionCount) {
                binding.nextButton.isVisible = false
                binding.prevButton.isVisible = false
                showResult()
            }
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            toggleAnswerButtons()
        }

        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
            toggleAnswerButtons()
        }

        updateQuestion()
        toggleAnswerButtons()
        if (quizViewModel.countAnsweredQuestions() == quizViewModel.totalQuestionCount) {
            binding.nextButton.isVisible = false
            binding.prevButton.isVisible = false
        }
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

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId: Int
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            quizViewModel.rightQuestionCount += 1
        } else {
            messageResId = R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }

    private fun toggleAnswerButtons() {
        if (quizViewModel.checkCurrentQuestionStatus()) {
            binding.trueButton.isVisible = false
            binding.falseButton.isVisible = false
        } else {
            binding.trueButton.isVisible = true
            binding.falseButton.isVisible = true
        }
    }

    private fun showResult() {
        val percentage = quizViewModel.calculateRightAnswersPercentage()
        val resultText = getString(R.string.result_toast, percentage)
        Toast.makeText(
            this,
            resultText,
            Toast.LENGTH_SHORT
        ).show()
    }
}