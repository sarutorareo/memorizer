package com.example.jirou.memorizer.models

import android.content.Context
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import com.example.jirou.memorizer.utils.stringArrayListWithoutBlankOf
import com.example.jirou.memorizer.utils.copyStringListToFixedSizeList

import org.jetbrains.anko.db.replaceOrThrow
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction

open class CorrectText(quizId: Int, correctList : List<String>) : Correct(quizId)  {
    private var mCorrectList : List<String> = correctList

    init {
    }

    val correctList : List<String>
        get() = mCorrectList

    override fun save(context: Context, dbName: String) {
        val thisInstance = this
        Log.e("QuestionText.save", String.format("save [%s]", thisInstance.toString()))
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        val cArray = copyStringListToFixedSizeList(correctList, 5)
        helper.use {
            transaction {
                replaceOrThrow(
                        MemorizeDBOpenHelper.TABLE_NAME_CRCT_TEXT,
                        *MemorizeDBOpenHelper.addUpdateDate(arrayOf(
                                "quiz_id" to quizId.toString(),
                                "correct_1" to cArray[0],
                                "correct_2" to cArray[1],
                                "correct_3" to cArray[2],
                                "correct_4" to cArray[3],
                                "correct_5" to cArray[4])                        )
                )
            }
        }
        Log.e("QuestionHandAction.save", "end save")
    }

    override fun load(context : Context, dbName: String) {
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        helper.use {
            try {
                val resultCorrectText: CorrectText = select(MemorizeDBOpenHelper.TABLE_NAME_CRCT_TEXT,
                        "correct_1",  "correct_2",  "correct_3",  "correct_4",  "correct_5")
                        .whereArgs("quiz_id = {quizId}", "quizId" to quizId.toString())
                        .parseSingle(
                                rowParser {
                                            correct_1: String, correct_2: String, correct_3: String,
                                            correct_4: String, correct_5: String ->
                                    val arr = stringArrayListWithoutBlankOf(correct_1, correct_2, correct_3, correct_4, correct_5)
                                    CorrectText(quizId, arr)
                                }
                        )
                copyFrom(resultCorrectText)
            }
            // 該当レコードが無い場合
            catch (e : SQLiteException) {
                // 何もしない
            }
        }
    }

    fun copyFrom(src : CorrectText) {
        mCorrectList = src.correctList.toList()
    }
}
