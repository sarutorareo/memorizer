package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestHandActionCorrectList {
    @Test
    fun constructor() {
        val hal = HandActionCorrectList()

        assertEquals(169, hal.size)

        var ha = hal.get(0)
        assertEquals(AV_FOLD_100, ha.getActionVal())
        assertEquals("AA", ha.getHand())
    }

    @Test
    fun getCorrect() {
        val hal = HandActionCorrectList()
        var ha : HandActionCorrect = hal.getCorrect(0)
        assertEquals(AV_FOLD_100, ha.getActionVal())
        assertEquals("AA", ha.getHand())
        assertEquals(null, ha.getCompared())
    }

}
