package com.example.jirou.memorizer.models

import android.content.Context
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import com.example.jirou.memorizer.db.MemorizeDBSQLDroidHelper
import com.example.jirou.memorizer.test_utils.TEST_DB_NAME
import com.example.jirou.memorizer.test_utils.DEFAULT_QUIZ_ID
import com.example.jirou.memorizer.test_utils.TestHelper
import org.jetbrains.anko.db.DEFAULT

import org.jetbrains.anko.db.insertOrThrow
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class TestCorrectText {
    private var mContext : Context = TestHelper.getContext()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MemorizeDBSQLDroidHelper.dropDBSchema(mContext, TEST_DB_NAME)
        MemorizeDBSQLDroidHelper.initDBSchema(mContext, TEST_DB_NAME)
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            insertOrThrow(MemorizeDBOpenHelper.TABLE_NAME_QUIZ, *MemorizeDBOpenHelper.addUpdateDate(arrayOf("id" to DEFAULT_QUIZ_ID.toString(), "type" to "test_type")))
        }
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.close()
    }

    @Test
    fun test_constructor_id() {
        val list = arrayListOf("a", "b", "c")
        val c = CorrectText(9, list)

        assertEquals(9, c.quizId)
        assertEquals(3, c.correctList.size)
        assertEquals("a", c.correctList[0])
        assertEquals("b", c.correctList[1])
        assertEquals("c", c.correctList[2])
    }

    @Test
    fun test_save() {
        //
        // Setup
        //
        val qId = DEFAULT_QUIZ_ID
        val list = arrayListOf("q1", "q2", "q3", "q4", "q5")
        val c = CorrectText(qId, list)

        //
        // Execute
        //
        c.save(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            val quizList = select(MemorizeDBOpenHelper.TABLE_NAME_CRCT_TEXT,
                    "quiz_id", "correct_1", "correct_2", "correct_3", "correct_4", "correct_5")                    .parseList(
                            rowParser {
                                quizId: Int,
                                correct_1: String, correct_2: String, correct_3: String, correct_4: String, correct_5: String ->
                                CorrectText(quizId, arrayListOf(correct_1, correct_2, correct_3, correct_4, correct_5))
                            }
                    )
            assertEquals(1, quizList.size)
            assertEquals(qId, quizList[0].quizId)
            assertEquals(5, quizList[0].correctList.size)
            assertEquals("q1", quizList[0].correctList[0])
            assertEquals("q2", quizList[0].correctList[1])
            assertEquals("q3", quizList[0].correctList[2])
            assertEquals("q4", quizList[0].correctList[3])
            assertEquals("q5", quizList[0].correctList[4])        }
    }


    @Test
    fun test_save_4correct() {
        //
        // Setup
        //
        val qId = DEFAULT_QUIZ_ID
        val list = arrayListOf("q1", "q2", "q3", "q4")
        val c = CorrectText(qId, list)

        //
        // Execute
        //
        c.save(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            val quizList = select(MemorizeDBOpenHelper.TABLE_NAME_CRCT_TEXT,
                    "quiz_id", "correct_1", "correct_2", "correct_3", "correct_4", "correct_5")                    .parseList(
                    rowParser {
                        quizId: Int,
                        correct_1: String, correct_2: String, correct_3: String, correct_4: String, correct_5: String ->
                        CorrectText(quizId, arrayListOf(correct_1, correct_2, correct_3, correct_4, correct_5))
                    }
            )
            assertEquals(1, quizList.size)
            assertEquals(qId, quizList[0].quizId)
            assertEquals(5, quizList[0].correctList.size)
            assertEquals("q1", quizList[0].correctList[0])
            assertEquals("q2", quizList[0].correctList[1])
            assertEquals("q3", quizList[0].correctList[2])
            assertEquals("q4", quizList[0].correctList[3])
            assertEquals("", quizList[0].correctList[4])        }
    }

    @Test
    fun test_load() {
        //
        // Setup
        //
        val qId = DEFAULT_QUIZ_ID
        val list = arrayListOf("q1", "q2", "q3", "q4", "q5")
        val c1 = CorrectText(qId, list)
        c1.save(mContext, TEST_DB_NAME)
        val c2 = CorrectText(qId, arrayListOf())

        //
        // Execute
        //
        c2.load(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        assertEquals(qId, c2.quizId)
        assertEquals(c1.correctList.size, c2.correctList.size)
        assertEquals(c1.correctList[0], c2.correctList[0])
        assertEquals(c1.correctList[1], c2.correctList[1])
        assertEquals(c1.correctList[2], c2.correctList[2])
        assertEquals(c1.correctList[3], c2.correctList[3])
        assertEquals(c1.correctList[4], c2.correctList[4])
    }


    @Test
    fun test_copyFrom() {
        //
        // Setup
        //
        val qId = DEFAULT_QUIZ_ID
        val list = arrayListOf("q1", "q2", "q3", "q4", "q5")
        val c1 = CorrectText(qId, list)
        c1.save(mContext, TEST_DB_NAME)
        val c2 = CorrectText(qId, arrayListOf())

        //
        // Execute
        //
        c2.copyFrom(c1)

        //
        // Execute
        //
        assertEquals(c1.correctList.size, c2.correctList.size)
        assertEquals(c1.correctList[0], c2.correctList[0])
        assertEquals(c1.correctList[1], c2.correctList[1])
        assertEquals(c1.correctList[2], c2.correctList[2])
        assertEquals(c1.correctList[3], c2.correctList[3])
        assertEquals(c1.correctList[4], c2.correctList[4])
    }
}
