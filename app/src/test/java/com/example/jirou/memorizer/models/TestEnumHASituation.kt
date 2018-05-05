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
    fun test_toHandActionSituation() {
        assertEquals(EnumHASituation.OPEN, EnumHASituation.toHandActionSituation("OPEN"))
        assertEquals(EnumHASituation.FACING_A_RAISE, EnumHASituation.toHandActionSituation("FACING_A_RAISE"))
        assertEquals(EnumHASituation.FACING_A_3BET, EnumHASituation.toHandActionSituation("FACING_A_3BET"))
        assertEquals(EnumHASituation.FACING_A_4BET, EnumHASituation.toHandActionSituation("FACING_A_4BET"))
    }

    @Test(expected = Exception::class)
    fun test_toHandActionSituation__except() {
        assertEquals(EnumHASituation.FACING_A_4BET, EnumHASituation.toHandActionSituation("FACING_A_4BETx"))
    }
}
