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
        assertEquals(1, EnumHASituation.VS_RAISE.rawValue)
        assertEquals(2, EnumHASituation.VS_3BET.rawValue)
        assertEquals(3, EnumHASituation.VS_4BET.rawValue)
    }

    @Test
    fun test_fromString() {
        assertEquals(EnumHASituation.OPEN, EnumHASituation.fromString("OPEN"))
        assertEquals(EnumHASituation.VS_RAISE, EnumHASituation.fromString("VS_RAISE"))
        assertEquals(EnumHASituation.VS_3BET, EnumHASituation.fromString("VS_3BET"))
        assertEquals(EnumHASituation.VS_4BET, EnumHASituation.fromString("VS_4BET"))
    }

    @Test(expected = AssertionError::class)
    fun test_fromString__except() {
        assertEquals(EnumHASituation.VS_4BET, EnumHASituation.fromString("VS_4BETx"))
    }

    @Test
    fun test_fromInt() {
        assertEquals(EnumHASituation.OPEN, EnumHASituation.fromInt(0))
        assertEquals(EnumHASituation.VS_RAISE, EnumHASituation.fromInt(1))
        assertEquals(EnumHASituation.VS_3BET, EnumHASituation.fromInt(2))
        assertEquals(EnumHASituation.VS_4BET, EnumHASituation.fromInt(3))
    }

    @Test(expected = AssertionError::class)
    fun test_fromInt__except() {
        assertEquals(EnumHASituation.VS_4BET, EnumHASituation.fromInt(4))
    }

}