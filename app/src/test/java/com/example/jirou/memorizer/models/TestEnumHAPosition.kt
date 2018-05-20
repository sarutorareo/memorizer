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
    fun test_rawValue() {
        assertEquals(0, EnumHAPosition.UTG.rawValue)
        assertEquals(1, EnumHAPosition.HJ.rawValue)
        assertEquals(2, EnumHAPosition.CO.rawValue)
        assertEquals(3, EnumHAPosition.BTN.rawValue)
        assertEquals(4, EnumHAPosition.SB.rawValue)
        assertEquals(5, EnumHAPosition.BB.rawValue)
        assertEquals(6, EnumHAPosition.NULL.rawValue)
    }

    @Test
    fun test_fromString() {
        assertEquals(EnumHAPosition.BTN, EnumHAPosition.fromString("BTN"))
        assertEquals(EnumHAPosition.SB, EnumHAPosition.fromString("SB"))
        assertEquals(EnumHAPosition.BB, EnumHAPosition.fromString("BB"))
        assertEquals(EnumHAPosition.UTG, EnumHAPosition.fromString("UTG"))
        assertEquals(EnumHAPosition.HJ, EnumHAPosition.fromString("HJ"))
        assertEquals(EnumHAPosition.CO, EnumHAPosition.fromString("CO"))
        assertEquals(EnumHAPosition.NULL, EnumHAPosition.fromString("NULL"))
    }

    @Test(expected = AssertionError::class)
    fun test_fromString__except() {
        assertEquals(EnumHAPosition.BTN, EnumHAPosition.fromString("BTNx"))
    }

    @Test
    fun test_fromInt() {
        assertEquals(EnumHAPosition.UTG, EnumHAPosition.fromInt(0))
        assertEquals(EnumHAPosition.HJ, EnumHAPosition.fromInt(1))
        assertEquals(EnumHAPosition.CO, EnumHAPosition.fromInt(2))
        assertEquals(EnumHAPosition.BTN, EnumHAPosition.fromInt(3))
        assertEquals(EnumHAPosition.SB, EnumHAPosition.fromInt(4))
        assertEquals(EnumHAPosition.BB, EnumHAPosition.fromInt(5))
        assertEquals(EnumHAPosition.NULL, EnumHAPosition.fromInt(6))
    }

    @Test(expected = AssertionError::class)
    fun test_fromInt__except() {
        assertEquals(EnumHAPosition.BTN, EnumHAPosition.fromInt(-1))
    }
}
