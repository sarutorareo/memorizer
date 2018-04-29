package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestQuiz {
    @Test
    fun constructor() {
        val q = Quiz()

        assertEquals(0, q.id)
        assertEquals(0, q.question.quizId)
        assertEquals(0, q.correct.quizId)
    }

    @Test
    fun constructor_id() {
        val q = Quiz(9)

        assertEquals(9, q.id)
        assertEquals(9, q.question.quizId)
        assertEquals(9, q.correct.quizId)
    }
}
