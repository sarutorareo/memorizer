package com.example.jirou.memorizer.models

open class CorrectHandAction(mQuizId: Int) : Correct(mQuizId)  {
    init {
    }
    private val mHandActionList = HandActionList()
    val handActionList : HandActionList
        get () {
            return mHandActionList
        }
}
