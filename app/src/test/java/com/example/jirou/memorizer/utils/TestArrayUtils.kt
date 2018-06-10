package com.example.jirou.memorizer.utils

import org.junit.Test

import org.junit.Assert.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestArrayUtils {
    @Test
    fun test_stringArrayWithoutBlankOf() {
        var ar = stringArrayListWithoutBlankOf("abc", "def", "")
        assertEquals(2, ar.size)
        assertEquals("abc", ar[0])
        assertEquals("def", ar[1])

        ar = stringArrayListWithoutBlankOf("abc", "", "ghi")
        assertEquals(2, ar.size)
        assertEquals("abc", ar[0])
        assertEquals("ghi", ar[1])

        ar = stringArrayListWithoutBlankOf("abc", null, "ghi")
        assertEquals(2, ar.size)
        assertEquals("abc", ar[0])
        assertEquals("ghi", ar[1])
    }

    @Test
    fun test_copyStringListToFixedSizeList() {
        var ar = copyStringListToFixedSizeList(arrayListOf("abc", "", "ghi"), 5)

        assertEquals(5, ar.size)
        assertEquals("abc", ar[0])
        assertEquals("", ar[1])
        assertEquals("ghi", ar[2])
        assertEquals("", ar[3])
        assertEquals("", ar[4])
    }
}