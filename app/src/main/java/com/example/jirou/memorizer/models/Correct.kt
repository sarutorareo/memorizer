package com.example.jirou.memorizer.models

import android.view.inputmethod.CorrectionInfo
import com.example.jirou.memorizer.utils.numToStr

open class Correct(private val mQuizId: Int)  {
    init {
    }

    val quizId : Int
        get() {
            return mQuizId
        }
}
