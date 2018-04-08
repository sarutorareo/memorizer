package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestHandAction {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun constructor() {
        val ha = HandAction("title_str", 99)

        assertEquals("title_str", ha.getHand())
        assertEquals(99, ha.getActionVal())
    }

}
