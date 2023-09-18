package com.coolrinet.geoquiz

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.coolrinet.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.remainingHints -= 1
            updateRemainingHints()
            toggleCheatButton()
        }
    }

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
            toggleCheatButton()

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
            toggleCheatButton()

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
            toggleCheatButton()
        }

        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
            toggleAnswerButtons()
            toggleCheatButton()
        }

        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        updateQuestion()
        updateRemainingHints()
        toggleAnswerButtons()
        toggleCheatButton()

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
        val messageResId = when {
            quizViewModel.isCheater -> {
                quizViewModel.rightQuestionCount += 1
                R.string.judgment_toast
            }
            userAnswer == correctAnswer -> {
                quizViewModel.rightQuestionCount += 1
                R.string.correct_toast
            }
            else -> R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }

    private fun toggleAnswerButtons() {
        val currentQuestionStatus = quizViewModel.checkCurrentQuestionStatus()
        binding.trueButton.isVisible = !currentQuestionStatus
        binding.falseButton.isVisible = !currentQuestionStatus
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

    private fun updateRemainingHints() {
        val remainingHints = quizViewModel.remainingHints
        val remainingHintsText = getString(R.string.remaining_hints, remainingHints)
        binding.remainingHints.text = remainingHintsText
    }

    private fun toggleCheatButton() {
        binding.cheatButton.isVisible =
            !(quizViewModel.isCheater || quizViewModel.remainingHints == 0
                    || quizViewModel.checkCurrentQuestionStatus())
    }
}