package com.coolrinet.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.coolrinet.geoquiz.databinding.ActivityCheatBinding

const val EXTRA_ANSWER_SHOWN = "com.coolrinet.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE =
    "com.coolrinet.geoquiz.answer_is_true"
private const val ANSWER_TEXT_KEY = "ANSWER_TEXT_KEY"

class CheatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheatBinding

    private var answerIsTrue = false
    private var answerText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        binding.showAnswerButton.setOnClickListener {
            answerText = when {
                answerIsTrue -> getString(R.string.true_button)
                else -> getString(R.string.false_button)
            }
            binding.answerTextView.text = answerText
            setAnswerShownResult(true)
        }

        updateAPIVersion()
    }

    private fun updateAPIVersion() {
        val apiVersion = Build.VERSION.SDK_INT
        val apiVersionText = getString(R.string.api_version, apiVersion)
        binding.apiVersionTextView.text = apiVersionText
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ANSWER_TEXT_KEY, answerText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        answerText = savedInstanceState.getString(ANSWER_TEXT_KEY)
        if (answerText != null) {
            binding.answerTextView.text = answerText
            setAnswerShownResult(true)
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}