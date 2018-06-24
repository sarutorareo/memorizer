package com.example.jirou.memorizer.models

import com.example.jirou.memorizer.InputStartingHandActivity

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

    override val type: EnumQuizType
        get() = EnumQuizType.HAND_ACTION

    override val title: String
        get() = (question as QuestionHandAction).title

    override val activity : Class<*>
        get() = InputStartingHandActivity::class.java

    override val requestCode : EnumRequestCodes
        get() = EnumRequestCodes.TRAINING_HAND_ACTION
}
