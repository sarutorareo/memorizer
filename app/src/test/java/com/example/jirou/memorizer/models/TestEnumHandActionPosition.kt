package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestEnumHandActionPosition {
    @Test
    fun constructor() {
        assertEquals("BB",  EnumHandActionPosition.BB.toString())
        assertEquals("BB",  EnumHandActionPosition.BB.name)
        assertEquals(5,  EnumHandActionPosition.BB.ordinal)

        assertEquals("UTG",  EnumHandActionPosition.UTG.name)
        assertEquals(0,  EnumHandActionPosition.UTG.ordinal)
    }

}
