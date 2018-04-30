package com.example.jirou.memorizer.models

open class QuestionHandAction(quizId: Int) : Question(quizId)  {

    private val mSituation : EnumHandActionSituation = EnumHandActionSituation.OPEN
    private val mHeroPos : EnumHandActionPosition = EnumHandActionPosition.BTN
    private val mOpponentPos   : EnumHandActionPosition = EnumHandActionPosition.NULL

    init {
    }

    val situation : EnumHandActionSituation
        get() = mSituation

    val heroPos : EnumHandActionPosition
        get() = mHeroPos

    val opponentPos : EnumHandActionPosition
        get() = mOpponentPos
}
