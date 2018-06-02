package com.example.jirou.memorizer.models

import android.content.Context
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import org.jetbrains.anko.db.replaceOrThrow
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction

class Score(private val mQuizId: Int)  {
    private var mAnswerNum = 0
    private var mCorrectNum = 0
    private var mUpdateDate = ""

    init {
    }

    val quizId : Int
        get() = mQuizId

    var answerNum : Int
        get() = mAnswerNum
        set(v) {
            mAnswerNum = v
        }

    var correctNum : Int
        get() = mCorrectNum
        set(v) {
            mCorrectNum = v
        }

    var updateDate : String
        get() = mUpdateDate
        set(v) {
            mUpdateDate = v
        }

    private val correctRatio : Double
        get() = if (mAnswerNum == 0) 0.0 else  mCorrectNum / mAnswerNum.toDouble()

    override fun toString() : String{
        return String.format("%d/%d %.0f%c", correctNum, answerNum, correctRatio * 100, '%')
    }

    fun save(context : Context, dbName: String) {
        Log.e("Score.save", "start save")

        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        helper.use {
            transaction {
                replaceOrThrow (
                        MemorizeDBOpenHelper.TABLE_NAME_SCORE,
                        *MemorizeDBOpenHelper.addUpdateDate(arrayOf("quiz_id" to quizId.toString(),
                                "answer_num" to answerNum.toString(),
                                "correct_num" to correctNum.toString()))
                )
            }
        }

        Log.e("Score.save", "end save")
    }

    fun load(context : Context, dbName: String) {
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        val srcScore = this
        helper.use {
            try {
                val loadedScore = select(MemorizeDBOpenHelper.TABLE_NAME_SCORE,
                        "quiz_id", "answer_num", "correct_num", "update_date")
                        .whereArgs("quiz_id = {quizId}", "quizId" to quizId.toString())
                        .parseSingle(
                                rowParser { quiz_id: Int, answer_num: Int, correct_num: Int, update_date: String ->
                                    val score = Score(quiz_id)
                                    score.answerNum = answer_num
                                    score.correctNum = correct_num
                                    score.updateDate = update_date
                                    score
                                }
                        )
                srcScore.copyFrom(loadedScore)
            }
            // 該当レコードが無い場合
            catch (e : SQLiteException) {
                // 何もしない
            }
        }
    }

    fun copyFrom(other : Score) {
        answerNum = other.answerNum
        correctNum = other.correctNum
        updateDate = other.updateDate
    }
}
