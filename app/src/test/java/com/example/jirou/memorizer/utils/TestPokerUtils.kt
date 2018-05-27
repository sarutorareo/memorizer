package com.example.jirou.memorizer.utils

import org.junit.Test
import org.junit.Assert

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestPokerUtils {

    @Test
    fun test_numToChar() {
        Assert.assertEquals('2', numToChar(2))
        Assert.assertEquals('9', numToChar(9))
        Assert.assertEquals('A', numToChar(14))
    }

    @Test
    fun test_getMaxChar() {
        Assert.assertEquals('A', getMaxChar("KA"))
        Assert.assertEquals('K', getMaxChar("KQ"))
        Assert.assertEquals('Q', getMaxChar("TQ"))
        Assert.assertEquals('J', getMaxChar("JT"))
        Assert.assertEquals('T', getMaxChar("9T"))
        Assert.assertEquals('9', getMaxChar("98"))
    }

    @Test
    fun test_getMinChar() {
        Assert.assertEquals('K', getMinChar("KA"))
        Assert.assertEquals('Q', getMinChar("KQ"))
        Assert.assertEquals('T', getMinChar("TQ"))
        Assert.assertEquals('T', getMinChar("JT"))
        Assert.assertEquals('9', getMinChar("9T"))
        Assert.assertEquals('8', getMinChar("98"))
    }
}