package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestEnumHAPosition {
    @Test
    fun constructor() {
        assertEquals("BB",  EnumHAPosition.BB.toString())
        assertEquals("BB",  EnumHAPosition.BB.name)
        assertEquals(5,  EnumHAPosition.BB.ordinal)

        assertEquals("UTG",  EnumHAPosition.UTG.name)
        assertEquals(0,  EnumHAPosition.UTG.ordinal)
    }

    @Test
    fun test_toHandActionPosition() {
        assertEquals(EnumHAPosition.BTN, EnumHAPosition.toHandActionPosition("BTN"))
        assertEquals(EnumHAPosition.SB, EnumHAPosition.toHandActionPosition("SB"))
        assertEquals(EnumHAPosition.BB, EnumHAPosition.toHandActionPosition("BB"))
        assertEquals(EnumHAPosition.UTG, EnumHAPosition.toHandActionPosition("UTG"))
        assertEquals(EnumHAPosition.HJ, EnumHAPosition.toHandActionPosition("HJ"))
        assertEquals(EnumHAPosition.CO, EnumHAPosition.toHandActionPosition("CO"))
        assertEquals(EnumHAPosition.NULL, EnumHAPosition.toHandActionPosition("NULL"))
    }

    @Test(expected = Exception::class)
    fun test_toHandActionSituation__except() {
        assertEquals(EnumHAPosition.BTN, EnumHAPosition.toHandActionPosition("BTNx"))
    }

}
