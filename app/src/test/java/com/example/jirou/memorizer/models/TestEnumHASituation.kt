package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestEnumHandActionSituation {
    @Test
    fun test_constructor() {
        assertEquals("OPEN",  EnumHandActionSituation.OPEN.toString())
    }

    @Test
    fun test_toHandActionSituation() {
        assertEquals(EnumHandActionSituation.OPEN, EnumHandActionSituation.toHandActionSituation("OPEN"))
        assertEquals(EnumHandActionSituation.FACING_A_RAISE, EnumHandActionSituation.toHandActionSituation("FACING_A_RAISE"))
        assertEquals(EnumHandActionSituation.FACING_A_3BET, EnumHandActionSituation.toHandActionSituation("FACING_A_3BET"))
        assertEquals(EnumHandActionSituation.FACING_A_4BET, EnumHandActionSituation.toHandActionSituation("FACING_A_4BET"))
    }

    @Test(expected = Exception::class)
    fun test_toHandActionSituation__except() {
        assertEquals(EnumHandActionSituation.FACING_A_4BET, EnumHandActionSituation.toHandActionSituation("FACING_A_4BETx"))
    }
}
