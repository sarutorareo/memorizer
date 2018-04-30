package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestHandActionComparedList {
    @Test
    fun constructor() {
        val hal = HandActionComparedList()

        assertEquals(169, hal.size)

        var ha = hal.get(0)
        assertEquals(AV_FOLD_100, ha.actionVal)
        assertEquals("AA", ha.hand)
    }

    @Test
    fun getCorrect() {
        val hal = HandActionComparedList()
        var ha : HandActionCompared = hal.getCorrect(0)
        assertEquals(AV_FOLD_100, ha.actionVal)
        assertEquals("AA", ha.hand)
        assertEquals(null, ha.getCompared())
    }

}
