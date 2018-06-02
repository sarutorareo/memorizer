package com.example.jirou.memorizer.models

import android.content.Context
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import com.example.jirou.memorizer.db.MemorizeDBSQLDroidHelper
import com.example.jirou.memorizer.test_utils.TEST_DB_NAME
import com.example.jirou.memorizer.test_utils.TestHelper
import com.example.jirou.memorizer.utils.dateToString
import org.jetbrains.anko.db.insertOrThrow
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

private const val DEFAULT_QUIZ_ID = 0

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class TestScore {
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
    fun test_toString() {
        //
        // Setup
        //
        val id = 999
        val s = Score(id)

        //
        // Execute & Verify
        //
        assertEquals("0/0 0%", s.toString())

        s.answerNum = 2
        s.correctNum = 1

        assertEquals("1/2 50%", s.toString())
    }

    @Test
    fun test_save() {
        //
        // Setup
        //
        val s = Score(DEFAULT_QUIZ_ID)
        s.answerNum = 2
        s.correctNum = 1

        //
        // Execute
        //
        s.save(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            val quizList = select(MemorizeDBOpenHelper.TABLE_NAME_SCORE,
                    "quiz_id", "answer_num", "correct_num", "update_date")
                    .parseList(
                            rowParser { quiz_id: Int, answer_num: Int, correct_num: Int, update_date: String ->
                                val score = Score(quiz_id)
                                score.answerNum = answer_num
                                score.correctNum = correct_num
                                score.updateDate = update_date
                                score
                            }
                    )
            assertEquals(1, quizList.size)
            assertEquals(DEFAULT_QUIZ_ID, quizList[0].quizId)
            assertEquals(2, quizList[0].answerNum)
            assertEquals(1, quizList[0].correctNum)

            // 現在時刻（ミリ秒を丸める）
            val currentDate = Date(dateToString(Date(System.currentTimeMillis())))
            val resultUpdateDate = Date(quizList[0].updateDate)
            val diffToUpdateLong: Long = currentDate.time - resultUpdateDate.time
            assertEquals(true, diffToUpdateLong >= 0)
            assertEquals(true, diffToUpdateLong / 1000 < 1)
        }
    }

    @Test
    fun test_load() {
        //
        // Setup
        //
        val s = Score(DEFAULT_QUIZ_ID)
        s.answerNum = 4
        s.correctNum = 3
        s.save(mContext, TEST_DB_NAME)

        //
        // Execute
        //
        val result = Score(DEFAULT_QUIZ_ID)
        result.load(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        assertEquals(DEFAULT_QUIZ_ID, result.quizId)
        assertEquals(4, result.answerNum)
        assertEquals(3, result.correctNum)

        // 現在時刻（ミリ秒を丸める）
        val currentDate = Date(dateToString(Date(System.currentTimeMillis())))
        var resultUpdateDate = Date(result.updateDate)
        var diffToUpdateLong: Long = currentDate.time - resultUpdateDate.time
        assertEquals(true, diffToUpdateLong >= 0)
        assertEquals(true, diffToUpdateLong / 1000 < 1)
    }

    @Test
    fun test_copyFrom() {
        //
        // Setup
        //
        val s = Score(1)
        s.answerNum = 4
        s.correctNum = 3
        s.updateDate = "1900/1/1 13:15:01"

        val sDist = Score(99)

        //
        // Execute
        //
        sDist.copyFrom(s)

        //
        // Verify
        //
        assertEquals(99, sDist.quizId)
        assertEquals(4, sDist.answerNum)
        assertEquals(3, sDist.correctNum)
        assertEquals("1900/1/1 13:15:01", sDist.updateDate)
    }
}
