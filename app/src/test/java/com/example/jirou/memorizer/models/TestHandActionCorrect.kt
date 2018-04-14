package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestHandActionCorrect {
    @Test
    fun constructor() {
        val ha = HandActionCorrect("title_str", 99)

        assertEquals("title_str", ha.getHand())
        assertEquals(99, ha.getActionVal())
        assertEquals(null, ha.getCompared())
    }

    @Test
    fun compare_equal() {
        val haC = HandActionCorrect("title_str", 99)
        val haA = HandAction("title_str", 99)

        haC.compare(haA)
        assertEquals(0, haC.getCompared())
    }

    @Test
    fun compare_grater() {
        val haC = HandActionCorrect("title_str", 101)
        val haA = HandAction("title_str", 99)

        haC.compare(haA)
        assertEquals(2, haC.getCompared())
    }

    @Test
    fun compare_lesser() {
        val haC = HandActionCorrect("title_str", 95)
        val haA = HandAction("title_str", 99)

        haC.compare(haA)
        assertEquals(-4, haC.getCompared())
    }
}
