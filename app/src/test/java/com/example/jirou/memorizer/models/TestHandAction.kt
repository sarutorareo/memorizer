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
    fun constructor() {
        val ha = HandAction("title_str", 99)

        assertEquals("title_str", ha.getHand())
        assertEquals(99, ha.getActionVal())
    }

    @Test
    fun copyTo() {
        val ha = HandAction("title_str", 99)
        val other = HandAction("title_str", 33)

        other.copyFrom(ha)
        assertEquals(99, ha.getActionVal())
        assertEquals(99, other.getActionVal())
    }

    @Test(expected = AssertionError::class)
    fun copyTo_differentKey() {
        val ha = HandAction("title_str", 99)
        val other = HandAction("title_strx", 99)

        other.copyFrom(ha)
    }

}
