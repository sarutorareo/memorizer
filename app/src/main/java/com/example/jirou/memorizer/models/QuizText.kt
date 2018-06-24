package com.example.jirou.memorizer.models

import com.example.jirou.memorizer.InputTextActivity

open class QuizText(id : Int) : Quiz(id)  {
    init {
        mQuestion = QuestionText(id)
        mCorrect = CorrectText(id, ArrayList())
    }

    override fun mCreateQuestion(id : Int) : Question
    {
        return QuestionText(id)
    }

    override fun mCreateCorrect(id : Int) : Correct
    {
        return CorrectText(id, ArrayList())
    }

    override val type: EnumQuizType
        get() = EnumQuizType.TEXT

    override val title: String
        get() = (question as QuestionText).title

    override val activity : Class<*>
        get() = InputTextActivity::class.java

    override val requestCode : EnumRequestCodes
        get() = EnumRequestCodes.TRAINING_TEXT
}
