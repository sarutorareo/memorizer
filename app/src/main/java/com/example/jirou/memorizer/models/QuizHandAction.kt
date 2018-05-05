package com.example.jirou.memorizer.models

open class QuizHandAction(id : Int) : Quiz(id)  {
    init {
        mQuestion = QuestionHandAction(id)
        mCorrect = CorrectHandAction(id)
    }

    override fun mCreateQuestion(id : Int) : Question
    {
        return QuestionHandAction(id)
    }

    override fun mCreateCorrect(id : Int) : Correct
    {
        return CorrectHandAction(id)
    }

    override fun mGetTypeStr(): String {
        return EnumQuizType.HAND_ACTION.toString()
    }

}
