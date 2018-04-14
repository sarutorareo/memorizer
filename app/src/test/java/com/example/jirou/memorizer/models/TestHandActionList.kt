package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestHandActionList {
    @Test
    fun constructor() {
        val hal = HandActionList()

        assertEquals(169, hal.size)

        var ha = hal.get(0)
        assertEquals("AA", ha.getHand())
        assertEquals(AV_FOLD_100, ha.getActionVal())

        ha = hal.get(1)
        assertEquals("AK", ha.getHand())
        assertEquals(AV_FOLD_100, ha.getActionVal())

        ha = hal.get(168)
        assertEquals("22", ha.getHand())
        assertEquals(AV_FOLD_100, ha.getActionVal())
    }

    @Test
    fun get() {
        val hal = HandActionList()
        var ha = hal.get(0)
        assertEquals("AA", ha.getHand())
        assertEquals(AV_FOLD_100, ha.getActionVal())

        ha = hal.get(168)
        assertEquals("22", ha.getHand())
        assertEquals(AV_FOLD_100, ha.getActionVal())
    }

    @Test(expected = AssertionError::class)
    fun get_outOfBounds_upper() {
        val hal = HandActionList()
        var ha = hal.get(169)
    }

    @Test(expected = AssertionError::class)
    fun get_outOfBounds_lower() {
        val hal = HandActionList()
        var ha = hal.get(-1)
    }
}
