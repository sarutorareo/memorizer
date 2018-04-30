package com.example.jirou.memorizer.models

import android.content.Context
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import com.example.jirou.memorizer.db.MemorizeDBSQLDroidHelper
import com.example.jirou.memorizer.test_utils.TEST_DB_NAME
import com.example.jirou.memorizer.test_utils.TestHelper
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insertOrThrow
import org.junit.Test

import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
private const val DEFAULT_QUIZ_ID = 0

@RunWith(RobolectricTestRunner::class)
class TestCorrectHandAction {
    private var mContext : Context = TestHelper.getContext()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MemorizeDBSQLDroidHelper.dropDBSchema(mContext, TEST_DB_NAME)
        MemorizeDBSQLDroidHelper.initDBSchema(mContext, TEST_DB_NAME)
        val helper = MemorizeDBOpenHelper.getInstance(mContext, TEST_DB_NAME)
        helper.use {
            /*
            var resultList = select(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    "id")
                    .parseList(
                            rowParser { id: Int->
                                id
                            }
                    )
            Log.e("setUp", String.format("size = %d", resultList.size))

            delete(MemorizeDBOpenHelper.TABLE_NAME_QUIZ)

            resultList = select(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    "id")
                    .parseList(
                            rowParser { id: Int->
                                id
                            }
                    )
            Log.e("setUp", String.format("size = %d", resultList.size))
*/
            delete(MemorizeDBOpenHelper.TABLE_NAME_QUIZ)
            insertOrThrow(MemorizeDBOpenHelper.TABLE_NAME_QUIZ, *MemorizeDBOpenHelper.addCreateUpdateDate(arrayOf("id" to DEFAULT_QUIZ_ID.toString(), "type" to "test_type")))
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

        val c = CorrectHandAction(9)

        assertEquals(9, c.quizId)
        assertEquals(169, c.handActionList.size)

//        helper.close()
    }

    @Test
    fun test_save() {


        val c = CorrectHandAction(quizId = DEFAULT_QUIZ_ID)
        c.handActionList.get(0).setActionVal(99)
        c.handActionList.get(1).setActionVal(98)

        // セーブ
        c.save(mContext, TEST_DB_NAME)

        c.handActionList.get(0).setActionVal(0)
        c.handActionList.get(1).setActionVal(0)

        // ロード
        c.load(mContext, TEST_DB_NAME)

        assertEquals(DEFAULT_QUIZ_ID, c.quizId)
        assertEquals(169, c.handActionList.size)
        assertEquals(99, c.handActionList.get(0).actionVal)
        assertEquals(98, c.handActionList.get(1).actionVal)

//        helper.close()
    }
}
