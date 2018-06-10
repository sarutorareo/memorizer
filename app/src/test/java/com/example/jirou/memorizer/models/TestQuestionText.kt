
package com.example.jirou.memorizer.models

import android.content.Context
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import com.example.jirou.memorizer.db.MemorizeDBSQLDroidHelper
import com.example.jirou.memorizer.test_utils.TEST_DB_NAME
import com.example.jirou.memorizer.test_utils.TestHelper
import org.jetbrains.anko.db.insertOrThrow
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

private const val DEFAULT_QUIZ_ID = 0

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class TestQuestionText {
    private var mContext: Context = TestHelper.getContext()

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
    fun test_save__insert() {
        //
        // Setup
        //
        val qId = DEFAULT_QUIZ_ID
        val qha = QuestionText(qId, "mainText", arrayListOf("q1", "q2", "q3", "q4", "q5"))

        //
        // Execute
        //
        qha.save(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            val quizList = select(MemorizeDBOpenHelper.TABLE_NAME_QST_TEXT,
                    "quiz_id", "question_main", "question_1", "question_2", "question_3", "question_4", "question_5")
                    .parseList(
                            rowParser {
                                quizId: Int, question_main: String,
                                question_1: String, question_2: String, question_3: String, question_4: String, question_5: String ->
                                QuestionText(quizId, question_main,
                                        arrayListOf(question_1, question_2, question_3, question_4, question_5))
                            }
                    )
            assertEquals(1, quizList.size)
            assertEquals(qId, quizList[0].quizId)
            assertEquals("mainText", quizList[0].questionMain)
            assertEquals(5, quizList[0].questionList.size)
            assertEquals("q1", quizList[0].questionList[0])
            assertEquals("q2", quizList[0].questionList[1])
            assertEquals("q3", quizList[0].questionList[2])
            assertEquals("q4", quizList[0].questionList[3])
            assertEquals("q5", quizList[0].questionList[4])
        }
    }

    @Test
    fun test_load() {
        //
        // Setup
        //
        val qId = DEFAULT_QUIZ_ID
        val qT = QuestionText(qId, "mainText", arrayListOf("q1", "q2", "q3", "q4", "q5"))
        qT.save(mContext, TEST_DB_NAME)
        val qT2 = QuestionText(qId, "mainText2", arrayListOf("q1-2"))

        //
        // Execute
        //
        qT2.load(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        assertEquals(qT.questionMain, qT2.questionMain)
        assertEquals(qT.questionList.size, qT2.questionList.size)
        assertEquals(qT.questionList[0], qT2.questionList[0])
        assertEquals(qT.questionList[1], qT2.questionList[1])
        assertEquals(qT.questionList[2], qT2.questionList[2])
        assertEquals(qT.questionList[3], qT2.questionList[3])
        assertEquals(qT.questionList[4], qT2.questionList[4])
    }

    @Test
    fun test_save__insert__4question() {
        //
        // Setup
        //
        val qId = DEFAULT_QUIZ_ID
        val qT = QuestionText(qId, "mainText", arrayListOf("q1", "q2", "q3", "q4"))
        val qT2 = QuestionText(qId, "a", arrayListOf("1", "2", "3", "4", "5"))
        qT.save(mContext, TEST_DB_NAME)

        //
        // Execute
        //
        qT2.load(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        assertEquals(qId, qT2.quizId)
        assertEquals("mainText", qT2.questionMain)
        assertEquals(4, qT2.questionList.size)
        assertEquals("q1", qT2.questionList[0])
        assertEquals("q2", qT2.questionList[1])
        assertEquals("q3", qT2.questionList[2])
        assertEquals("q4", qT2.questionList[3])
    }

    @Test
    fun test_copyFrom() {
        //
        // Setup
        //
        val qId = DEFAULT_QUIZ_ID
        val qFrom = QuestionText(qId, "mainText", arrayListOf("q1", "q2", "q3", "q4", "q5"))
        val qTo = QuestionText(qId + 1, "mainText2", arrayListOf("q12", "q22", "q32", "q42"))

        //
        // Execute
        //
        qTo.copyFrom(qFrom)

        //
        // Verify
        //
        assertEquals(qId + 1, qTo.quizId)
        assertEquals("mainText", qTo.questionMain)
        assertEquals(5, qTo.questionList.size)
        assertEquals("q1", qTo.questionList[0])
        assertEquals("q2", qTo.questionList[1])
        assertEquals("q3", qTo.questionList[2])
        assertEquals("q4", qTo.questionList[3])
        assertEquals("q5", qTo.questionList[4])
    }
}