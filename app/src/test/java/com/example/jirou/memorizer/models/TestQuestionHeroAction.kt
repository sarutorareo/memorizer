package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestQuestionHeroAction {
    @Test
    fun constructor_id() {
        val q = QuestionHandAction(9)

        assertEquals(9, q.quizId)
        assertEquals(EnumHandActionSituation.OPEN, q.situation)
        assertEquals(EnumHandActionPosition.BTN, q.heroPos)
        assertEquals(EnumHandActionPosition.NULL, q.opponentPos)
    }
}
