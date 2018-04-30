package com.example.jirou.memorizer.db

import android.content.Context
import android.database.DatabaseUtils
import android.util.Log
import com.example.jirou.memorizer.test_utils.TestHelper
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Connection

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(RobolectricTestRunner::class)
class TestMemorizerDBSQLDroidHelper {

    @Test
    fun test_createTable_java() {
        val context = TestHelper.getContext()
        var conn: Connection? = null
        var statement: Statement? = null
        var resultSet: ResultSet? = null
        val databaseName = "testDB.db"

        try {
            DatabaseUtils.createDbFromSqlStatements(context, databaseName, 1, "");
            val url = "jdbc:sqlite:/" + context.getDatabasePath(databaseName)
            conn = DriverManager.getConnection(url)
            statement = conn?.createStatement()
            statement?.executeUpdate("CREATE TABLE test (id  int, msg varchar(32), PRIMARY KEY(id, msg))")

            resultSet = statement?.executeQuery("select * from test")
        } catch (e : Exception) {
            Log.e("test_createTable_java", e.toString())
        } finally {
            resultSet?.close()
            statement?.close()
            conn?.close()
        }
    }
}
