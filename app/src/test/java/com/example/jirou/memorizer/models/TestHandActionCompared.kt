package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestHandActionCompared {
    @Test
    fun constructor() {
        val ha = HandActionCompared("AK", 99)

        assertEquals("AK", ha.hand)
        assertEquals(99, ha.actionVal)
        assertEquals(null, ha.getCompared())
    }

    @Test
    fun compare_equal() {
        val haC = HandActionCompared("AK", 99)
        val haA = HandAction("AK", 99)

        haC.compare(haA)
        assertEquals(0, haC.getCompared())
    }

    @Test
    fun compare_grater() {
        val haC = HandActionCompared("AK", 101)
        val haA = HandAction("AK", 99)

        haC.compare(haA)
        assertEquals(2, haC.getCompared())
    }

    @Test
    fun compare_lesser() {
        val haC = HandActionCompared("AK", 95)
        val haA = HandAction("QJ", 99)

        haC.compare(haA)
        assertEquals(-4, haC.getCompared())
    }
}
