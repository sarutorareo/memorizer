package com.example.jirou.memorizer.models

open class Quiz()  {
    private var mId : Int = 0
    protected var mQuestion : Question = Question(mId)
    protected var mCorrect : Correct = Correct(mId)

    init {
    }

    constructor(id : Int) : this() {
        mId = id
        mQuestion = Question(id)
        mCorrect = Correct(id)
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

}
