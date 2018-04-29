package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestQuizHandAction {
    @Test
    fun constructor() {
        val q = QuizHandAction(9)

        assertEquals(9, q.id)

        assertEquals(9, q.question.quizId)
        assertEquals(true, q.question is QuestionHandAction)
        val question : QuestionHandAction = q.question as QuestionHandAction
        assertEquals(EnumHandActionSituation.OPEN, question.situation)
        assertEquals(EnumHandActionPosition.BTN, question.heroPos )
        assertEquals(EnumHandActionPosition.NULL, question.opponentPos )

        assertEquals(9, q.correct.quizId)
        assertEquals(true, q.correct is CorrectHandAction)
        val correct : CorrectHandAction = q.correct as CorrectHandAction
        assertEquals(169, correct.handActionList.size)
    }

}
