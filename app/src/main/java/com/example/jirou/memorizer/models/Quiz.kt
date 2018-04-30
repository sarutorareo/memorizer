package com.example.jirou.memorizer.models

import android.annotation.SuppressLint

abstract class Quiz()  {
    private var mId : Int = 0
    protected var mQuestion : Question = mCreateQuestion(mId)
    protected var mCorrect : Correct = mCreateCorrect(mId)

    init {
    }

    constructor(id : Int) : this() {
        mId = id
        mQuestion = mCreateQuestion(id)
        mCorrect = mCreateCorrect(id)
    }

    val id : Int
        get() {
            return mId
        }

    val question : Question
        get() {
            return mQuestion
        }

    val correct : Correct
        get() {
            return mCorrect
        }

    protected abstract fun mCreateQuestion(id : Int) : Question
    protected abstract fun mCreateCorrect(id : Int) : Correct
}
