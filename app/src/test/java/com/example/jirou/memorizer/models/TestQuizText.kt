
package com.example.jirou.memorizer.models

import android.content.Context
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import com.example.jirou.memorizer.db.MemorizeDBSQLDroidHelper
import com.example.jirou.memorizer.test_utils.TEST_DB_NAME
import com.example.jirou.memorizer.test_utils.TestHelper
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.Thread.sleep
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class TestQuizText {
    private var mContext: Context = TestHelper.getContext()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MemorizeDBSQLDroidHelper.dropDBSchema(mContext, TEST_DB_NAME)
        MemorizeDBSQLDroidHelper.initDBSchema(mContext, TEST_DB_NAME)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.close()
    }

    @Test
    fun constructor() {
        val q = QuizText(9)

        assertEquals(9, q.id)
        assertEquals(EnumQuizType.TEXT, q.type)
        assertEquals("", q.updateDate)

        assertEquals(true, q.question is QuestionText)
        assertEquals(true, q.correct is CorrectText)
    }
}