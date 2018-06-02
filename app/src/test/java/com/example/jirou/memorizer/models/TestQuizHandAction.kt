
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
class TestQuizHandAction {
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
        val q = QuizHandAction(9)

        assertEquals(9, q.id)
        assertEquals(EnumQuizType.HAND_ACTION, q.type)
        assertEquals("", q.updateDate)

        assertEquals(9, q.question.quizId)
        assertEquals(true, q.question is QuestionHandAction)
        val question: QuestionHandAction = q.question as QuestionHandAction
        assertEquals(EnumHASituation.OPEN, question.situation)
        assertEquals(EnumHAPosition.BTN, question.heroPosition)
        assertEquals(EnumHAPosition.NULL, question.opponentPosition)

        assertEquals(9, q.correct.quizId)
        assertEquals(true, q.correct is CorrectHandAction)
        val correct: CorrectHandAction = q.correct as CorrectHandAction
        assertEquals(169, correct.handActionList.size)
    }

    @Test
    fun test_save__insert() {
        //
        // Setup
        //
        val id = 999
        val q = QuizHandAction(id)
        val hand1 = (q.correct as CorrectHandAction).handActionList.get(1).hand
        val val1 = 2
        val ansNum = 5
        (q.question as QuestionHandAction).situation = EnumHASituation.VS_4BET
        (q.question as QuestionHandAction).heroPosition = EnumHAPosition.BB
        (q.question as QuestionHandAction).opponentPosition = EnumHAPosition.SB
        (q.correct as CorrectHandAction).handActionList.getFromHand(hand1).setActionVal(val1)
        q.score.answerNum = ansNum

        //
        // Execute
        // セーブ
        //
        q.save(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            val quizList = select(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    "id", "type")
                    .parseList(
                            rowParser { id: Int, type: String ->
                                Pair(id, type)
                            }
                    )
            assertEquals(1, quizList.size)
            assertEquals(id, quizList[0].first)
            assertEquals(EnumQuizType.HAND_ACTION.toString(), quizList[0].second)

            val qstHa = select(MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION,
                    "situation", "hero_position", "opponent_position")
                    .whereArgs("quiz_id = {quizId}", "quizId" to id.toString())
                    .parseSingle(
                            rowParser {
                                situation: String, heroPos: String, opponentPos: String ->
                                QuestionHandAction(id,
                                        EnumHASituation.fromString(situation),
                                        EnumHAPosition.fromString(heroPos),
                                        EnumHAPosition.fromString(opponentPos)
                                )
                            }
                    )

            assertEquals(EnumHASituation.VS_4BET, qstHa.situation)
            assertEquals(EnumHAPosition.BB, qstHa.heroPosition)
            assertEquals(EnumHAPosition.SB, qstHa.opponentPosition)

            val handActionList = select(MemorizeDBOpenHelper.TABLE_NAME_CRCT_HAND_ACTION_ITEM,
                    "hand", "action_val")
                    .whereArgs("quiz_id = {quizId}", "quizId" to id.toString())
                    .parseList(
                            rowParser { hand: String, actionVal: Int ->
                                HandAction(hand, actionVal)
                            }
                    )
            assertEquals(169, handActionList.size)
            var ha1: HandAction? = null
            handActionList.forEach()
            {
                if (it.hand == hand1) {
                    ha1 = it
                }
            }
            assertEquals(true, ha1 != null)
            assertEquals(val1, ha1!!.actionVal)

            val answerNumList = select(MemorizeDBOpenHelper.TABLE_NAME_SCORE,
                    "answer_num")
                    .whereArgs("quiz_id = {quizId}", "quizId" to id.toString())
                    .parseList(
                            rowParser { answer_num: Int ->
                                answer_num
                            }
                    )
            assertEquals(1, answerNumList.size)
            assertEquals(ansNum, answerNumList[0])
        }
    }

    @Test
    fun test_save__replace() {
        //
        // Setup
        //
        val id = 999
        val q = QuizHandAction(id)
        val hand1 = (q.correct as CorrectHandAction).handActionList.get(1).hand
        val val1 = 2
        val ansNum = 5

        (q.correct as CorrectHandAction).handActionList.getFromHand(hand1).setActionVal(val1)
        q.score.answerNum = ansNum

        // セーブ(insert)
        q.save(mContext, TEST_DB_NAME)

        // 値を書き換え
        val val2 = val1 + 1
        val ansNum2 = ansNum + 1
        (q.correct as CorrectHandAction).handActionList.getFromHand(hand1).setActionVal(val2)
        (q.question as QuestionHandAction).situation = EnumHASituation.VS_4BET
        (q.question as QuestionHandAction).heroPosition = EnumHAPosition.BB
        (q.question as QuestionHandAction).opponentPosition = EnumHAPosition.SB
        q.score.answerNum = ansNum2

        sleep(1000)
        //
        // Execute
        // セーブ ( replace )
        //
        q.save(mContext, TEST_DB_NAME)

        //
        // Verify
        //
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            val quizList = select(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    "id", "type", "update_date")
                    .parseList(
                            rowParser { id: Int, type: String, update_date  : String->
                                Triple(id, type, update_date)
                            }
                    )
            assertEquals(1, quizList.size)
            assertEquals(id, quizList[0].first)
            assertEquals(EnumQuizType.HAND_ACTION.toString(), quizList[0].second)
            val resultUpdateDate = Date(quizList[0].third)
            val currentDate = Date(System.currentTimeMillis())
            val diffToUpdateLong: Long = currentDate.time - resultUpdateDate.time
            assertEquals(true, diffToUpdateLong / 1000 < 1)

            val qstHaAndUpdateDate = select(MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION,
                    "situation", "hero_position", "opponent_position", "update_date")
                    .whereArgs("quiz_id = {quizId}", "quizId" to id.toString())
                    .parseSingle(
                            rowParser {
                                situation: String, heroPos: String, opponentPos: String, updateDate: String ->
                                Pair(QuestionHandAction(id,
                                        EnumHASituation.fromString(situation),
                                        EnumHAPosition.fromString(heroPos),
                                        EnumHAPosition.fromString(opponentPos)),
                                        updateDate)
                            }
                    )
            val qstHa = qstHaAndUpdateDate.first
            val resultQstHaDate =  Date(qstHaAndUpdateDate.second)
            val diffToQstHaUpdateLong: Long = currentDate.time - resultQstHaDate.time
            assertEquals(true, diffToQstHaUpdateLong / 1000 < 1)

            assertEquals(EnumHASituation.VS_4BET, qstHa.situation)
            assertEquals(EnumHAPosition.BB, qstHa.heroPosition)
            assertEquals(EnumHAPosition.SB, qstHa.opponentPosition)

            val handActionList = select(MemorizeDBOpenHelper.TABLE_NAME_CRCT_HAND_ACTION_ITEM,
                    "hand", "action_val", "update_date")
                    .parseList(
                            rowParser { hand: String, actionVal: Int, updateDate : String->
                                Pair(HandAction(hand, actionVal), updateDate)
                            }
                    )
            assertEquals(169, handActionList.size)
            var ha1 :HandAction? = null
            handActionList.forEach()
            {
                if (it.first.hand == hand1) {
                    ha1 = it.first
                    val resultHaItemDate =  Date(it.second)
                    val diffToHaItemUpdateLong: Long = currentDate.time - resultHaItemDate.time
                    assertEquals(true, diffToHaItemUpdateLong / 1000 < 1)
                }
            }
            assertEquals(true,  ha1 != null)
            assertEquals(val2, ha1!!.actionVal)

            val answerNumList = select(MemorizeDBOpenHelper.TABLE_NAME_SCORE,
                    "answer_num")
                    .whereArgs("quiz_id = {quizId}", "quizId" to id.toString())
                    .parseList(
                            rowParser { answer_num: Int ->
                                answer_num
                            }
                    )
            assertEquals(1, answerNumList.size)
            assertEquals(ansNum2, answerNumList[0])
        }
    }

    @Test
    fun test_title_open() {
        //
        // Setup
        //
        val id = 999
        val q = QuizHandAction(id)
        val qst = q.question as QuestionHandAction
        qst.situation = EnumHASituation.OPEN
        qst.heroPosition = EnumHAPosition.BTN
        qst.opponentPosition = EnumHAPosition.NULL

        //
        // Execute & Verify
        //
        assertEquals("OPEN-BTN-NULL", q.title)
    }

    @Test
    fun test_title__open() {
        //
        // Setup
        //
        val id = 999
        val q = QuizHandAction(id)
        val qst = q.question as QuestionHandAction
        qst.situation = EnumHASituation.OPEN
        qst.heroPosition = EnumHAPosition.BTN
        qst.opponentPosition = EnumHAPosition.NULL

        //
        // Execute & Verify
        //
        assertEquals("OPEN-BTN-NULL", q.title)
    }

    @Test
    fun test_title__facing_a_4bet() {
        //
        // Setup
        //
        val id = 999
        val q = QuizHandAction(id)
        val qst = q.question as QuestionHandAction
        qst.situation = EnumHASituation.VS_4BET
        qst.heroPosition = EnumHAPosition.BB
        qst.opponentPosition = EnumHAPosition.SB

        //
        // Execute & Verify
        //
        assertEquals("VS_4BET-BB-SB", q.title)
    }
}