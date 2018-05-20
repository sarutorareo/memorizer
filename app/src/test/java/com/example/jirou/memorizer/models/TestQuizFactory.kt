package com.example.jirou.memorizer.models

import android.content.Context
import org.junit.Assert.*
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import com.example.jirou.memorizer.db.MemorizeDBSQLDroidHelper
import com.example.jirou.memorizer.test_utils.TEST_DB_NAME
import com.example.jirou.memorizer.test_utils.TestHelper
import com.example.jirou.memorizer.utils.dateToString
import org.jetbrains.anko.db.insertOrThrow
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class TestQuizFactory {
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
    fun test_load() {
        //
        // setup
        //
        val quizId = 99
        val situation1 = EnumHASituation.FACING_A_3BET
        val heroPos1 = EnumHAPosition.HJ
        val opponentPos1 = EnumHAPosition.UTG

        val hand1 = "AK"
        val val1 = 3

        // 現在時刻（ミリ秒を丸める）
        val currentDate = Date(dateToString(Date(System.currentTimeMillis())))
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            insertOrThrow(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    *MemorizeDBOpenHelper.addUpdateDate(
                            arrayOf("id" to quizId.toString(), "type" to EnumQuizType.HAND_ACTION.toString())))

            insertOrThrow(MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION,
                    *MemorizeDBOpenHelper.addUpdateDate(
                            arrayOf("quiz_id" to quizId.toString(),
                                    "situation" to situation1.toString(),
                                    "hero_position" to heroPos1.toString(),
                                    "opponent_position" to opponentPos1.toString()
                            )))

            insertOrThrow(MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION_ITEM,
                    *MemorizeDBOpenHelper.addUpdateDate(
                            arrayOf("quiz_id" to quizId.toString(), "hand" to hand1, "action_val" to val1.toString())))
        }

        //
        // execute
        //
        val q: Quiz = QuizFactory().load(mContext, TEST_DB_NAME, quizId)

        //
        // verify
        //
        assertEquals(true, q is QuizHandAction)
        val resultUpdateDate = Date(q.updateDate)
        val diffToUpdateLong: Long = currentDate.time - resultUpdateDate.time
        assertEquals(true, diffToUpdateLong >= 0)
        assertEquals(true, diffToUpdateLong / 1000 < 1)

        val qstHa = (q.question as QuestionHandAction)
        assertEquals(EnumHASituation.FACING_A_3BET, qstHa.situation)
        val handActionList = (q.correct as CorrectHandAction).handActionList
        assertEquals(val1, handActionList.getFromHand(hand1).actionVal)
    }

    @Test(expected = AssertionError::class)
    fun test_load__exception_no_type() {
        val quizId = 99
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            insertOrThrow(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    *MemorizeDBOpenHelper.addUpdateDate(
                            arrayOf("id" to quizId.toString(), "type" to "test_type")))
        }

        val q: Quiz = QuizFactory().load(mContext, TEST_DB_NAME, quizId)

        assertEquals(true, q is QuizHandAction)
    }

    @Test(expected = Exception::class)
    fun test_load__exception_no_id() {
        val quizId = 99
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            insertOrThrow(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    *MemorizeDBOpenHelper.addUpdateDate(
                            arrayOf("id" to quizId.toString(), "type" to "HAND_ACTION")))
        }

        val q: Quiz = QuizFactory().load(mContext, TEST_DB_NAME, quizId + 1)

        assertEquals(true, q is QuizHandAction)
    }

    @Test
    fun test_loadOrCreate__load() {
        //
        // setup
        //
        val quizId = 99
        val quizHa = QuizHandAction(quizId)
        quizHa.save(mContext, TEST_DB_NAME)

        //
        // execute
        //
        val q: Quiz = QuizFactory().loadOrCreate(mContext, TEST_DB_NAME, quizId, EnumQuizType.HAND_ACTION)

        //
        // verify
        //
        assertEquals(true, q is QuizHandAction)
        assertEquals(quizId, q.id)
    }

    @Test
    fun test_loadOrCreate__create() {
        //
        // setup
        //
        val quizId = 99
        val quizHa = QuizHandAction(quizId)
        quizHa.save(mContext, TEST_DB_NAME)

        //
        // execute
        //
        val q: Quiz = QuizFactory().loadOrCreate(mContext, TEST_DB_NAME, quizId + 1, EnumQuizType.HAND_ACTION)

        //
        // verify
        //
        assertEquals(true, q is QuizHandAction)
        assertEquals(quizId + 1, q.id)
    }

    @Test
    fun test_createDefaultInstance() {
        val id = 4
        val q = QuizFactory().createDefaultInstance(id, EnumQuizType.HAND_ACTION)

        assertEquals(true, q is QuizHandAction)
        assertEquals(id, q.id)
    }

    @Test
    fun test_createQuizFromType() {
        val q = QuizFactory().createQuizFromType(EnumQuizType.HAND_ACTION, 5)
        assertEquals(true, q is QuizHandAction)
        assertEquals(5, q.id)
    }

    @Test
    fun test_loadAllList() {
        //
        // setup
        //
        // 現在時刻（ミリ秒を丸める）
        val currentDate = Date(dateToString(Date(System.currentTimeMillis())))
        var quizId = 99
        var quizHa = QuizHandAction(quizId)
        quizHa.save(mContext, TEST_DB_NAME)
        quizId = 100
        quizHa = QuizHandAction(quizId)
        quizHa.save(mContext, TEST_DB_NAME)

        //
        // execute
        //
        val qList: List<Quiz> = QuizFactory().loadAllList(mContext, TEST_DB_NAME)

        //
        // verify
        //
        assertEquals(2, qList.size)
        assertEquals(true, qList[0] is QuizHandAction)
        assertEquals(99, qList[0].id)
        var resultUpdateDate = Date(qList[0].updateDate)
        var diffToUpdateLong: Long = currentDate.time - resultUpdateDate.time
        assertEquals(true, diffToUpdateLong >= 0)
        assertEquals(true, diffToUpdateLong / 1000 < 1)

        assertEquals(true, qList[1] is QuizHandAction)
        assertEquals(100, qList[1].id)
        resultUpdateDate = Date(qList[1].updateDate)
        diffToUpdateLong = currentDate.time - resultUpdateDate.time
        assertEquals(true, diffToUpdateLong >= 0)
        assertEquals(true, diffToUpdateLong / 1000 < 1)
    }

}
