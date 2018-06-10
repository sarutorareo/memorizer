package com.example.jirou.memorizer.kotlinTest

import org.junit.Test
import org.junit.Assert

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestKotlin {
    @Test
    fun test_listCopy__reference() {
        val src = mutableListOf("a", "b", "c")
        val dst = src

        src[0] = "xx"
        Assert.assertEquals("xx", src[0])
        Assert.assertEquals("b", src[1])
        Assert.assertEquals("c", src[2])
        Assert.assertEquals("xx", dst[0])
        Assert.assertEquals("b", dst[1])
        Assert.assertEquals("c", dst[2])
    }

    @Test
    fun test_listCopy() {
        val src = mutableListOf("a", "b", "c")
        val dst = src.toList()

        src[0] = "xx"
        Assert.assertEquals("xx", src[0])
        Assert.assertEquals("b", src[1])
        Assert.assertEquals("c", src[2])
        Assert.assertEquals("a", dst[0])
        Assert.assertEquals("b", dst[1])
        Assert.assertEquals("c", dst[2])
    }

}