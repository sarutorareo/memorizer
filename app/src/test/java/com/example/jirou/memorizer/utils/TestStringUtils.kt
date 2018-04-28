package com.example.jirou.memorizer.utils

import org.junit.Test

import org.junit.Assert.*
import java.util.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestStringUtils {
    @Test
    fun test_dateToString() {
        val date = Date("2018/1/1 1:2:3")
        assertEquals("2018/01/01 01:02:03", dateToString(date))
    }
}