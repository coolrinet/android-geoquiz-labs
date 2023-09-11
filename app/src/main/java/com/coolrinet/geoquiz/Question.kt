package com.coolrinet.geoquiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean, var isAnswered: Boolean = false)