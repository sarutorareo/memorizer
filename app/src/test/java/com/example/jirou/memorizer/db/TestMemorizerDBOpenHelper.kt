package com.example.jirou.memorizer.db

import com.example.jirou.memorizer.test_utils.TEST_DB_NAME
import com.example.jirou.memorizer.test_utils.TestHelper
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.db.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
private const val TABLE_NAME_TEST_1 = "test_1"
private const val TABLE_NAME_TEST_2 = "test_2"

@RunWith(RobolectricTestRunner::class)
class TestMemorizerDBOpenHelper {

    @Test
    fun test_getInstance() {
        val context = TestHelper.getContext()
        val helper = MemorizeDBOpenHelper.getInstance(context = context, dbName = TEST_DB_NAME)
        assertEquals(TEST_DB_NAME, helper.databaseName)
    }

    class Test1(val id: Int, val msg: String)
    class Test2(val parent_id : Int, val id: Int, val msg: String)

    @Test
    fun test_select() {
        val context = TestHelper.getContext()
        val helper = MemorizeDBOpenHelper.getInstance(context = context, dbName = TEST_DB_NAME)
        helper.use {
            createTable(TABLE_NAME_TEST_1, ifNotExists = true,
                    columns = *arrayOf("id" to INTEGER + PRIMARY_KEY, "msg" to TEXT))

            insertOrThrow(
                    TABLE_NAME_TEST_1,
                    *arrayOf("id" to "1", "msg" to "aaa")
            )
            insertOrThrow(
                    TABLE_NAME_TEST_1,
                    *arrayOf("id" to "2", "msg" to "bbb")
            )

            val resultList = select(TABLE_NAME_TEST_1, "id", "msg").parseList(
                    rowParser { id: Int, msg: String->
                        Test1(id, msg)
                    }
            )
            assertEquals(2, resultList.size)
            assertEquals(1, resultList[0].id)
            assertEquals("aaa", resultList[0].msg)
            assertEquals(2, resultList[1].id)
            assertEquals("bbb", resultList[1].msg)

            dropTable(TABLE_NAME_TEST_1, ifExists = true)
        }
    }

    @Test
    fun test_transaction_rollback() {
        val context = TestHelper.getContext()
        val helper = MemorizeDBOpenHelper.getInstance(context = context, dbName = TEST_DB_NAME)
        helper.use {
            createTable(TABLE_NAME_TEST_1, ifNotExists = true,
                    columns = *arrayOf("id" to INTEGER + PRIMARY_KEY, "msg" to TEXT))
            createTable(TABLE_NAME_TEST_2, ifNotExists = true,
                    columns = *arrayOf("parent_id" to INTEGER, "id" to INTEGER, "msg" to TEXT))
            try {
                transaction {
                        insertOrThrow(
                                TABLE_NAME_TEST_1,
                                *arrayOf("id" to "1", "msg" to "aaa")
                        )
                        insertOrThrow(
                                TABLE_NAME_TEST_2 + "x",
                                *arrayOf("parent_id" to "1", "id" to "2", "msg" to "bbb")
                        )
                }
            } catch ( e: Exception) {

            }
            val resultList1 = select(TABLE_NAME_TEST_1, "id", "msg").parseList(
                    rowParser { id: Int, msg: String->
                        Test1(id, msg)
                    }
            )
            val resultList2 = select(TABLE_NAME_TEST_2, "parent_id", "id", "msg").parseList(
                    rowParser { parent_id: Int, id: Int, msg: String->
                        Test2(parent_id, id, msg)
                    }
            )

            assertEquals(0, resultList1.size)
            assertEquals(0, resultList2.size)

            dropTable(TABLE_NAME_TEST_1, ifExists = true)
            dropTable(TABLE_NAME_TEST_2, ifExists = true)
        }
    }

    @Test
    fun test_transaction_not_rollback() {
        val context = TestHelper.getContext()
        val helper = MemorizeDBOpenHelper.getInstance(context = context, dbName = TEST_DB_NAME)
        helper.use {
            createTable(TABLE_NAME_TEST_1, ifNotExists = true,
                    columns = *arrayOf("id" to INTEGER + PRIMARY_KEY, "msg" to TEXT))
            createTable(TABLE_NAME_TEST_2, ifNotExists = true,
                    columns = *arrayOf("parent_id" to INTEGER, "id" to INTEGER, "msg" to TEXT))
            transaction {
                try {
                    insertOrThrow(
                            TABLE_NAME_TEST_1,
                            *arrayOf("id" to "1", "msg" to "aaa")
                    )
                    insertOrThrow(
                            TABLE_NAME_TEST_2 + "x",
                            *arrayOf("parent_id" to "1", "id" to "2", "msg" to "bbb")
                    )
                } catch ( e: Exception) {

                }
            }
            val resultList1 = select(TABLE_NAME_TEST_1, "id", "msg").parseList(
                    rowParser { id: Int, msg: String->
                        Test1(id, msg)
                    }
            )
            val resultList2 = select(TABLE_NAME_TEST_2, "parent_id", "id", "msg").parseList(
                    rowParser { parent_id: Int, id: Int, msg: String->
                        Test2(parent_id, id, msg)
                    }
            )

            assertEquals(1, resultList1.size)
            assertEquals(0, resultList2.size)

            dropTable(TABLE_NAME_TEST_1, ifExists = true)
            dropTable(TABLE_NAME_TEST_2, ifExists = true)
        }
    }

    @Test
    fun test_transaction_nest() {
        val context = TestHelper.getContext()
        val helper = MemorizeDBOpenHelper.getInstance(context = context, dbName = TEST_DB_NAME)
        helper.use {
            createTable(TABLE_NAME_TEST_1, ifNotExists = true,
                    columns = *arrayOf("id" to INTEGER + PRIMARY_KEY, "msg" to TEXT))
            createTable(TABLE_NAME_TEST_2, ifNotExists = true,
                    columns = *arrayOf("parent_id" to INTEGER, "id" to INTEGER, "msg" to TEXT))
            try {
                transaction {
                    insertOrThrow(
                            TABLE_NAME_TEST_1,
                            *arrayOf("id" to "1", "msg" to "aaa")
                    )
                    transaction {
                        insertOrThrow(
                                TABLE_NAME_TEST_2 + "x",
                                *arrayOf("parent_id" to "1", "id" to "2", "msg" to "bbb")
                        )
                    }
                    insertOrThrow(
                            TABLE_NAME_TEST_1,
                            *arrayOf("id" to "2", "msg" to "aaa2")
                    )
                }
            } catch ( e: Exception) {

            }
            val resultList1 = select(TABLE_NAME_TEST_1, "id", "msg").parseList(
                    rowParser { id: Int, msg: String->
                        Test1(id, msg)
                    }
            )
            val resultList2 = select(TABLE_NAME_TEST_2, "parent_id", "id", "msg").parseList(
                    rowParser { parent_id: Int, id: Int, msg: String->
                        Test2(parent_id, id, msg)
                    }
            )

            assertEquals(0, resultList1.size)
            assertEquals(0, resultList2.size)

            dropTable(TABLE_NAME_TEST_1, ifExists = true)
            dropTable(TABLE_NAME_TEST_2, ifExists = true)
        }
    }

    @Test
    fun test_transaction_nest2() {
        val context = TestHelper.getContext()
        val helper = MemorizeDBOpenHelper.getInstance(context = context, dbName = TEST_DB_NAME)
        helper.use {
            createTable(TABLE_NAME_TEST_1, ifNotExists = true,
                    columns = *arrayOf("id" to INTEGER + PRIMARY_KEY, "msg" to TEXT))
            createTable(TABLE_NAME_TEST_2, ifNotExists = true,
                    columns = *arrayOf("parent_id" to INTEGER, "id" to INTEGER, "msg" to TEXT))
            try {
                transaction {
                    insertOrThrow(
                            TABLE_NAME_TEST_1,
                            *arrayOf("id" to "1", "msg" to "aaa")
                    )
                    transaction {
                        insertOrThrow(
                                TABLE_NAME_TEST_2,
                                *arrayOf("parent_id" to "1", "id" to "2", "msg" to "bbb")
                        )
                    }
                    insertOrThrow(
                            TABLE_NAME_TEST_1 + "x",
                            *arrayOf("id" to "2", "msg" to "aaa2")
                    )
                }
            } catch ( e: Exception) {

            }
            val resultList1 = select(TABLE_NAME_TEST_1, "id", "msg").parseList(
                    rowParser { id: Int, msg: String->
                        Test1(id, msg)
                    }
            )
            val resultList2 = select(TABLE_NAME_TEST_2, "parent_id", "id", "msg").parseList(
                    rowParser { parent_id: Int, id: Int, msg: String->
                        Test2(parent_id, id, msg)
                    }
            )

            assertEquals(0, resultList1.size)
            assertEquals(0, resultList2.size)

            dropTable(TABLE_NAME_TEST_1, ifExists = true)
            dropTable(TABLE_NAME_TEST_2, ifExists = true)
        }
    }

    // array に自動的にcreate, updateを追加するメソッド
    @Test
    fun test_addCreateUpdateDate() {
        val array = arrayOf("item1" to "aaa", "item2" to "bbb")
        assertEquals(2, array.size)
        val newArray = MemorizeDBOpenHelper.addCreateUpdateDate(array)

        assertEquals( array.size + 1, newArray.size)
        val currentDate = Date()
        newArray.forEachWithIndex { i, pair ->
            when {
                (i < array.size) -> {
                    assertEquals(array[i].first, pair.first)
                    assertEquals(array[i].second, pair.second)
                }
                (i == array.size) -> {
                    assertEquals("update_date", pair.first)
                    val resultDate = Date(pair.second)
                    val diffLong: Long = currentDate.time - resultDate.time
                    assertEquals(true, diffLong / 1000 <= 1)
                }
            }
        }
    }
}
