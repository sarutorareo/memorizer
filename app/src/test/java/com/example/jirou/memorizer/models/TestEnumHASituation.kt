package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestEnumHASituation {
    @Test
    fun test_constructor() {
        assertEquals("OPEN",  EnumHASituation.OPEN.toString())
    }

    @Test
    fun test_rawValue() {
        assertEquals(0, EnumHASituation.OPEN.rawValue)
        assertEquals(1, EnumHASituation.FACING_A_RAISE.rawValue)
        assertEquals(2, EnumHASituation.FACING_A_3BET.rawValue)
        assertEquals(3, EnumHASituation.FACING_A_4BET.rawValue)
    }

    @Test
    fun test_fromString() {
        assertEquals(EnumHASituation.OPEN, EnumHASituation.fromString("OPEN"))
        assertEquals(EnumHASituation.FACING_A_RAISE, EnumHASituation.fromString("FACING_A_RAISE"))
        assertEquals(EnumHASituation.FACING_A_3BET, EnumHASituation.fromString("FACING_A_3BET"))
        assertEquals(EnumHASituation.FACING_A_4BET, EnumHASituation.fromString("FACING_A_4BET"))
    }

    @Test(expected = AssertionError::class)
    fun test_fromString__except() {
        assertEquals(EnumHASituation.FACING_A_4BET, EnumHASituation.fromString("FACING_A_4BETx"))
    }

    @Test
    fun test_fromInt() {
        assertEquals(EnumHASituation.OPEN, EnumHASituation.fromInt(0))
        assertEquals(EnumHASituation.FACING_A_RAISE, EnumHASituation.fromInt(1))
        assertEquals(EnumHASituation.FACING_A_3BET, EnumHASituation.fromInt(2))
        assertEquals(EnumHASituation.FACING_A_4BET, EnumHASituation.fromInt(3))
    }

    @Test(expected = AssertionError::class)
    fun test_fromInt__except() {
        assertEquals(EnumHASituation.FACING_A_4BET, EnumHASituation.fromInt(4))
    }

}
