package com.example.jirou.memorizer.models

open class Question(private val mQuizId: Int)  {
    init {
    }

    val quizId : Int
        get() {
            return mQuizId
        }
}
