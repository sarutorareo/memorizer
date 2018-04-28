package com.example.jirou.memorizer.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*
import java.util.*
import com.example.jirou.memorizer.utils.dateToString

class MemorizeDBOpenHelper(context:Context):ManagedSQLiteOpenHelper(context,"testMemorizer.db",null,1)
{
    companion object {
        const val TABLE_NAME_TEST = "test"
        const val TABLE_NAME_QUIZ = "quiz"
        const val TABLE_NAME_QST_HAND_ACTION = "qst_hand_action"
        const val TABLE_NAME_QST_HAND_ACTION_ITEM = "qst_hand_action_item"

        private var instance: MemorizeDBOpenHelper? = null

        fun getInstance(context: Context): MemorizeDBOpenHelper {
            /*
            instance = instance ?: MemorizeDBOpenHelper(context.applicationContext)
            return instance!!
            */
            instance  =
                    if (instance == null) {
                        Log.e("getInstance", "instance is null")
                        MemorizeDBOpenHelper(context.applicationContext)
                    } else {
                        Log.e("getInstance", "instance is not null")
                        instance as MemorizeDBOpenHelper
                    }
            Log.e("getInstance", "before get readable database")

            return instance!!
        }

        fun initDBSchema(context: Context) {
            Log.e("DBHelper.initDBSchema", "start")
            val db = getInstance(context).readableDatabase
            Log.e("DBHelper.initDBSchema", String.format("db = %s", db.toString()))
            db?.run {
                createTable(TABLE_NAME_TEST, ifNotExists = true,
                        columns = *arrayOf("date" to TEXT, "bmi" to TEXT, "create_date" to TEXT + NOT_NULL, "update_date" to TEXT + NOT_NULL))
                createTable(TABLE_NAME_QUIZ, ifNotExists = true,
                    columns = *arrayOf( "id" to INTEGER  + PRIMARY_KEY /*+ AUTOINCREMENT*/, "type" to TEXT, "create_date" to TEXT + NOT_NULL, "update_date" to TEXT + NOT_NULL))
//                        columns = *arrayOf("id" to INTEGER, "type" to TEXT))
                /*
                createTable(TABLE_NAME_QST_HAND_ACTION, ifNotExists = true,
                        columns = *arrayOf( "quiz_id" to INTEGER  + PRIMARY_KEY + UNIQUE, "situation" to TEXT, "heroPosition" to TEXT, "oponentPosition" to TEXT))
                createTable(TABLE_NAME_QST_HAND_ACTION_ITEM, ifNotExists = true,
                        columns = *arrayOf( "quiz_id" to INTEGER  + PRIMARY_KEY + UNIQUE, "hand" to TEXT  + PRIMARY_KEY + UNIQUE, "actionVal" to INTEGER))
                 */
            }
        }

        fun dropDBSchema(context: Context) {
            Log.e("DBHelper.dropDBSchema", "start")
            val db = getInstance(context).readableDatabase
            Log.e("DBHelper.dropDBSchema", String.format("db = %s", db.toString()))
            db?.run {
                dropTable(TABLE_NAME_TEST, ifExists = true)
                dropTable(TABLE_NAME_QUIZ, ifExists = true)
            }
        }

        fun addCreateUpdateDate(ar: Array<Pair<String, String>>) : Array<Pair<String, String>> {
            val current = dateToString(Date(System.currentTimeMillis()))
            val dateArray = arrayOf("create_date" to current, "update_date" to current)

            return arrayOf(*ar, *dateArray)
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}