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
    fun test_constructor() {
        val ha = HandAction("title_str", 99)

        assertEquals("title_str", ha.hand)
        assertEquals(99, ha.actionVal)
    }

    @Test
    fun test_copyFrom() {
        val ha = HandAction("title_str", 99)
        val other = HandAction("title_str", 33)

        other.copyFrom(ha)
        assertEquals(99, ha.actionVal)
        assertEquals(99, other.actionVal)
    }

    @Test(expected = AssertionError::class)
    fun copyTo_differentKey() {
        val ha = HandAction("title_str", 99)
        val other = HandAction("title_strx", 99)

        other.copyFrom(ha)
    }

    @Test
    fun test_handDispStr() {
        var ha = HandAction("AK", 99)

        assertEquals("AK", ha.hand)
        assertEquals("AK", ha.handDispStr)

        ha = HandAction("KA", 99)

        assertEquals("KA", ha.hand)
        assertEquals("AK", ha.handDispStr)
    }

}
