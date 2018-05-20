package com.example.jirou.memorizer.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestEnumQuizType {
    @Test
    fun test_fromString() {
        assertEquals(EnumQuizType.HAND_ACTION, EnumQuizType.fromString("HAND_ACTION"))
    }

    @Test(expected = AssertionError::class)
    fun test_fromString__except() {
        assertEquals(EnumQuizType.HAND_ACTION, EnumQuizType.fromString("HAND_ACTIONx"))
    }

}
