package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestQuestion {
    @Test
    fun constructor_id() {
        val q = Question(9)

        assertEquals(9, q.quizId)
    }
}
