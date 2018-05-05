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

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class TestQuizHandAction {
    private var mContext : Context = TestHelper.getContext()

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

        assertEquals(9, q.question.quizId)
        assertEquals(true, q.question is QuestionHandAction)
        val question : QuestionHandAction = q.question as QuestionHandAction
        assertEquals(EnumHandActionSituation.OPEN, question.situation)
        assertEquals(EnumHandActionPosition.BTN, question.heroPos )
        assertEquals(EnumHandActionPosition.NULL, question.opponentPos )

        assertEquals(9, q.correct.quizId)
        assertEquals(true, q.correct is CorrectHandAction)
        val correct : CorrectHandAction = q.correct as CorrectHandAction
        assertEquals(169, correct.handActionList.size)

        assertEquals( true, q.isNewRecord )
    }

    @Test
    fun test_save() {
        //
        // Setup
        //
        val id = 999
        val q = QuizHandAction(id)
        val hand1 = (q.correct as CorrectHandAction).handActionList.get(1).hand
        val val1 = 2
        (q.correct as CorrectHandAction).handActionList.getFromHand(hand1).setActionVal(val1)

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
                            rowParser { id: Int, type: String->
                                Pair(id, type)
                            }
                    )
            assertEquals(1, quizList.size)
            assertEquals(id, quizList[0].first)
            assertEquals(EnumQuizType.HAND_ACTION.toString(), quizList[0].second)


            val handActionList = select(MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION_ITEM,
                    "hand", "action_val")
                    .parseList(
                            rowParser { hand: String, actionVal: Int->
                                HandAction(hand, actionVal)
                            }
                    )
            assertEquals(169, handActionList.size)
            var ha1 :HandAction? = null
            handActionList.forEach()
            {
                if (it.hand == hand1) {
                    ha1 = it
                }
            }
            assertEquals(true,  ha1 != null)
            assertEquals(val1, ha1!!.actionVal)
        }
    }
}
