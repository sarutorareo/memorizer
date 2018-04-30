package com.example.jirou.memorizer.db

import android.content.Context
import android.database.DatabaseUtils
import android.util.Log
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper.Companion.TABLE_NAME_QUIZ
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper.Companion.TABLE_NAME_QST_HAND_ACTION_ITEM
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper.Companion.TABLE_NAME_TEST
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement


class MemorizeDBSQLDroidHelper  {
    companion object {
        fun initDBSchema(context: Context, dbName : String) {
            Class.forName("org.sqldroid.SQLDroidDriver");
            var conn: Connection? = null
            var statement: Statement? = null

            Log.e("DBHelper.initDBSchema", "start")
            try {
                DatabaseUtils.createDbFromSqlStatements(context, dbName, 1, "");
                val url = "jdbc:sqldroid:/" + context.getDatabasePath(dbName)
                Log.e("DBHelper.initDBSchema", String.format("url = %s", url))
                conn = DriverManager.getConnection(url)
                statement = conn?.createStatement()

                statement?.execute ("PRAGMA foreign_keys=ON;")
                statement?.executeUpdate("CREATE TABLE IF NOT EXISTS $TABLE_NAME_TEST" +
                        " (date TEXT," +
                        " bmi TEXT," +
                        " create_date TEXT NOT NULL," +
                        " update_date TEXT NOT NULL," +
                        " PRIMARY KEY(date, bmi))")
                Log.e("DBHelper.initDBSchema", "table test created")
                statement?.executeUpdate( "CREATE TABLE IF NOT EXISTS $TABLE_NAME_QUIZ " +
                        "(id INTEGER NOT NULL, " +
                        " type TEXT NOT NULL," +
                        " create_date TEXT NOT NULL," +
                        " update_date TEXT NOT NULL," +
                        " PRIMARY KEY(id))")
                Log.e("DBHelper.initDBSchema", "table quiz created")
                statement?.executeUpdate( "CREATE TABLE IF NOT EXISTS $TABLE_NAME_QST_HAND_ACTION_ITEM " +
                        "(quiz_id INTEGER NOT NULL, " +
                        "hand TEXT NOT NULL, " +
                        "action_val INTEGER, " +
                        "create_date TEXT NOT NULL, " +
                        "update_date TEXT NOT NULL, " +
                        "PRIMARY KEY(quiz_id, hand)," +
                        "FOREIGN KEY(`quiz_id`) REFERENCES `quiz`(`id`) ON DELETE CASCADE" +
                        ")")
                Log.e("DBHelper.initDBSchema", "table quiz_hand_action created")
            } catch (e : Exception) {
                Log.e("initDBSchema", e.toString())
                throw e
            } finally {
                statement?.close()
                conn?.close()
            }
        }

        fun dropDBSchema(context: Context, dbName : String) {
            Class.forName("org.sqldroid.SQLDroidDriver");
            var conn: Connection? = null
            var statement: Statement? = null

            Log.e("DBHelper.dropDBSchema", "start")

            try {
                DatabaseUtils.createDbFromSqlStatements(context, dbName, 1, "");
                val url = "jdbc:sqldroid:/" + context.getDatabasePath(dbName)
                Log.e("DBHelper.dropDBSchema", String.format("url = %s", url))
                conn = DriverManager.getConnection(url)
                statement = conn?.createStatement()

                statement?.execute ("PRAGMA foreign_keys=ON;")
                statement?.executeUpdate("DROP TABLE IF EXISTS $TABLE_NAME_QST_HAND_ACTION_ITEM ")
                statement?.executeUpdate("DROP TABLE IF EXISTS $TABLE_NAME_QUIZ ")
                statement?.executeUpdate("DROP TABLE IF EXISTS $TABLE_NAME_TEST ")
            } catch (e : Exception) {
                Log.e("initDBSchema", e.toString())
                throw e
            } finally {
                statement?.close()
                conn?.close()
            }
        }

    }
}
