package com.example.jirou.memorizer.models

import android.content.Context

abstract class Correct(private val mQuizId: Int)  {
    init {
    }

    val quizId : Int
        get() {
            return mQuizId
        }

    abstract fun save(context : Context, dbName: String)
    abstract fun load(context : Context, dbName: String)
}
