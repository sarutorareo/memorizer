package com.example.jirou.memorizer.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*
import java.util.*
import com.example.jirou.memorizer.utils.dateToString

const val DB_NAME_MEMORIZER : String = "testMemorizer.db"

class MemorizeDBOpenHelper(context:Context, dbName: String):ManagedSQLiteOpenHelper(context, dbName,null,1)
{
    companion object {
        const val TABLE_NAME_TEST = "test"
        const val TABLE_NAME_QUIZ = "quiz"
        const val TABLE_NAME_QST_HAND_ACTION = "qst_hand_action"
        const val TABLE_NAME_CRCT_HAND_ACTION_ITEM = "crct_hand_action_item"

        private var instance: MemorizeDBOpenHelper? = null

        fun getInstance(context: Context, dbName: String): MemorizeDBOpenHelper {
            /*
            instance = instance ?: MemorizeDBOpenHelper(context.applicationContext)
            return instance!!
            */
            instance  =
                    if (instance == null) {
                        Log.e("getInstance", "instance is null")
                        MemorizeDBOpenHelper(context.applicationContext, dbName)
                    } else {
                        Log.e("getInstance", "instance is not null")
                        instance as MemorizeDBOpenHelper
                    }
            return instance!!
        }

        fun addUpdateDate(ar: Array<Pair<String, String>>) : Array<Pair<String, String>> {
            val current = dateToString(Date(System.currentTimeMillis()))
            val dateArray = arrayOf("update_date" to current)

            return arrayOf(*ar, *dateArray)
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }
}