package com.example.jirou.memorizer.models

import android.content.Context
import org.junit.Assert.*
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import com.example.jirou.memorizer.db.MemorizeDBSQLDroidHelper
import com.example.jirou.memorizer.test_utils.TEST_DB_NAME
import com.example.jirou.memorizer.test_utils.TestHelper
import org.jetbrains.anko.db.insertOrThrow
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

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
        val hand1 = "AK"
        val val1 = 3
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            insertOrThrow(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    *MemorizeDBOpenHelper.addCreateUpdateDate(
                            arrayOf("id" to quizId.toString(), "type" to EnumQuizType.HAND_ACTION.toString())))

            insertOrThrow(MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION_ITEM,
                    *MemorizeDBOpenHelper.addCreateUpdateDate(
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
        val handActionList = (q.correct as CorrectHandAction).handActionList
        assertEquals(val1, handActionList.getFromHand(hand1).actionVal)
    }

    @Test(expected = Exception::class)
    fun test_load_exception() {
        val quizId = 99
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            insertOrThrow(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    *MemorizeDBOpenHelper.addCreateUpdateDate(
                            arrayOf("id" to quizId.toString(), "type" to "test_type")))
        }

        val q: Quiz = QuizFactory().load(mContext, TEST_DB_NAME, quizId)

        assertEquals(true, q is QuizHandAction)
    }

    @Test
    fun test_loadOrCreate_load() {
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
    fun test_loadOrCreate_create() {
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
}
