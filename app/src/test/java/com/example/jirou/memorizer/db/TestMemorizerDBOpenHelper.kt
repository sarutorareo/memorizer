package com.example.jirou.memorizer.db

import android.content.Context
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.wrapContent
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class TestMemorizerDBOpenHelper {
    @Test
    fun test_getInstance() {
        val context = RuntimeEnvironment.application
        val db = MemorizeDBOpenHelper.getInstance(context = context)
        assertEquals("testMemorizer.db", db.databaseName)
    }

    // array に自動的にcreate, updateを追加するメソッド
    @Test
    fun test_addCreateUpdateDate() {
        val array = arrayOf("item1" to "aaa", "item2" to "bbb")
        assertEquals(2, array.size)
        val newArray = MemorizeDBOpenHelper.addCreateUpdateDate(array)

        assertEquals( array.size + 2, newArray.size)
        val currentDate = Date()
        newArray.forEachWithIndex { i, pair ->
            when {
                (i < array.size) -> {
                    assertEquals(array[i].first, pair.first)
                    assertEquals(array[i].second, pair.second)
                }
                (i == array.size) -> {
                    assertEquals("create_date", pair.first)
                    val resultDate = Date(pair.second)
                    val diffLong: Long = currentDate.time - resultDate.time
                    assertEquals(true, diffLong / 1000 <= 1)
                }
                (i == array.size + 1) -> {
                    assertEquals("update_date", pair.first)
                    val resultDate = Date(pair.second)
                    val diffLong: Long = currentDate.time - resultDate.time
                    assertEquals(true, diffLong / 1000 <= 1)
                }
            }
        }
    }
}
