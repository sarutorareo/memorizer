
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
class TestQuestionHandAction {
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
    fun test_constructor_id() {
        val q = QuestionHandAction(9)

        assertEquals(9, q.quizId)
        assertEquals(EnumHASituation.OPEN, q.situation)
        assertEquals(EnumHAPosition.BTN, q.heroPosition)
        assertEquals(EnumHAPosition.NULL, q.opponentPosition)
    }

    @Test
    fun test_save__insert() {
        //
        // Setup
        //
        val qId = DEFAULT_QUIZ_ID
        val qha = QuestionHandAction(qId, EnumHASituation.VS_RAISE, EnumHAPosition.CO, EnumHAPosition.HJ)

        //
        // Execute
        //
        qha.save(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            val quizList = select(MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION,
                    "quiz_id", "situation", "hero_position", "opponent_position")
                    .parseList(
                            rowParser {
                                quizId: Int, situation: String, heroPos: String, opponentPos: String ->
                                QuestionHandAction(quizId,
                                        EnumHASituation.fromString(situation),
                                        EnumHAPosition.fromString(heroPos),
                                        EnumHAPosition.fromString(opponentPos)
                                )
                            }
                    )
            assertEquals(1, quizList.size)
            assertEquals(qId, quizList[0].quizId)
            assertEquals(EnumHASituation.VS_RAISE, quizList[0].situation)
            assertEquals(EnumHAPosition.CO, quizList[0].heroPosition)
            assertEquals(EnumHAPosition.HJ, quizList[0].opponentPosition)
        }
    }

    @Test
    fun test_save__upload() {
        //
        // Setup
        //
        val qId = DEFAULT_QUIZ_ID
        val qha = QuestionHandAction(qId, EnumHASituation.VS_RAISE, EnumHAPosition.CO, EnumHAPosition.HJ)
        qha.save(mContext, TEST_DB_NAME)

        qha.situation = EnumHASituation.VS_3BET
        qha.heroPosition = EnumHAPosition.SB
        qha.opponentPosition = EnumHAPosition.BB

        //
        // Execute
        //
        qha.save(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            val quizList = select(MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION,
                    "quiz_id", "situation", "hero_position", "opponent_position")
                    .parseList(
                            rowParser {
                                quizId: Int, situation: String, heroPos: String, opponentPos: String ->
                                QuestionHandAction(quizId,
                                        EnumHASituation.fromString(situation),
                                        EnumHAPosition.fromString(heroPos),
                                        EnumHAPosition.fromString(opponentPos)
                                )
                            }
                    )
            assertEquals(1, quizList.size)
            assertEquals(qId, quizList[0].quizId)
            assertEquals(EnumHASituation.VS_3BET, quizList[0].situation)
            assertEquals(EnumHAPosition.SB, quizList[0].heroPosition)
            assertEquals(EnumHAPosition.BB, quizList[0].opponentPosition)
        }
    }

    @Test
    fun test_load() {
        //
        // Setup
        //
        val qId = DEFAULT_QUIZ_ID
        val qha = QuestionHandAction(qId, EnumHASituation.VS_RAISE, EnumHAPosition.CO, EnumHAPosition.HJ)
        qha.save(mContext, TEST_DB_NAME)

        qha.situation = EnumHASituation.VS_3BET
        qha.heroPosition = EnumHAPosition.SB
        qha.opponentPosition = EnumHAPosition.BB

        //
        // Execute
        //
        qha.load(mContext, TEST_DB_NAME)

        assertEquals(qId, qha.quizId)
        assertEquals(EnumHASituation.VS_RAISE, qha.situation)
        assertEquals(EnumHAPosition.CO, qha.heroPosition)
        assertEquals(EnumHAPosition.HJ, qha.opponentPosition)
    }

    @Test
    fun test_copyFrom() {
        val dst = QuestionHandAction(0, EnumHASituation.VS_3BET, EnumHAPosition.SB, EnumHAPosition.BTN)
        val src = QuestionHandAction(0, EnumHASituation.VS_4BET, EnumHAPosition.UTG, EnumHAPosition.CO )

        dst.copyFrom(src)
        assertEquals(EnumHASituation.VS_4BET,  dst.situation)
        assertEquals(EnumHAPosition.UTG,  dst.heroPosition)
        assertEquals(EnumHAPosition.CO,  dst.opponentPosition)
    }
}