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
        assertEquals("AA", ha.hand)
        assertEquals(AV_FOLD_100, ha.actionVal)

        ha = hal.get(1)
        assertEquals("AK", ha.hand)
        assertEquals(AV_FOLD_100, ha.actionVal)

        ha = hal.get(168)
        assertEquals("22", ha.hand)
        assertEquals(AV_FOLD_100, ha.actionVal)
    }

    @Test
    fun get() {
        val hal = HandActionList()
        var ha = hal.get(0)
        assertEquals("AA", ha.hand)
        assertEquals(AV_FOLD_100, ha.actionVal)

        ha = hal.get(168)
        assertEquals("22", ha.hand)
        assertEquals(AV_FOLD_100, ha.actionVal)
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

    @Test
    fun test_copyFrom() {
        val haList = HandActionList()
        var ha = haList.getFromHand("72")
        assertEquals("72", ha.hand)
        assertEquals(AV_FOLD_100, ha.actionVal)

        val newHand = HandAction("72", 99)
        haList.copyHandActionFrom(newHand)

        assertEquals("72", ha.hand)
        assertEquals(99, ha.actionVal)
    }

    @Test
    fun test_getFromHand() {
        val haList = HandActionList()
        var ha = haList.getFromHand("AK")
        assertEquals("AK", ha.hand)
    }

    @Test(expected = Exception::class)
    fun test_getFromHand_not_exist() {
        val haList = HandActionList()
        var ha = haList.getFromHand("AX")
    }
}
