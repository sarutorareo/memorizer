package com.example.jirou.memorizer.models

open class QuizHandAction(id : Int) : Quiz(id)  {
    init {
        mQuestion = QuestionHandAction(id)
        mCorrect = CorrectHandAction(id)
    }

}
