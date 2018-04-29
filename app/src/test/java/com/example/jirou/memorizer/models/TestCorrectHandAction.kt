package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestCorrectHandAction {
    @Test
    fun constructor_id() {
        val c = CorrectHandAction(9)

        assertEquals(9, c.quizId)
        assertEquals(169, c.handActionList.size)
    }
}
