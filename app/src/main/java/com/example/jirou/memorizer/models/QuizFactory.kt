package com.example.jirou.memorizer.models

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.jirou.memorizer.NEW_QUIZ_ID
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import org.jetbrains.anko.db.insertOrThrow
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction

class QuizFactory  {
    init {
    }

    fun load(context : Context, dbName: String, quiz_id : Int) : Quiz
    {
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        var result : Triple<Int, String, String>? = null
        helper.use {
            result = select(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    "id", "type", "update_date")
                    .whereArgs("id = {id}", "id" to quiz_id.toString())
                    .parseSingle(
                            rowParser { id: Int, type: String, update_date: String ->
                                Triple(id, type, update_date)
                            }
                    )

        }
        val id = result!!.first
        val type = result!!.second
        val quiz : Quiz
        quiz = createQuizFromType(EnumQuizType.fromString(type), id)
        quiz.updateDate =  result!!.third

        quiz.question.load(context, dbName)
        quiz.correct.load(context, dbName)

        return quiz
    }

    fun createQuizFromType(type: EnumQuizType, id: Int): Quiz {
        return when (type) {
            EnumQuizType.HAND_ACTION -> {
                QuizHandAction(id)
            }
        }
    }

    fun loadOrCreate(context : Context, dbName: String, quiz_id : Int, type : EnumQuizType) : Quiz
    {
        return try {
            load(context, dbName, quiz_id)
        }
        catch (e: SQLiteException) {
            createDefaultInstance(quiz_id, type)
        }
        catch (e: Exception) {
            throw e
        }
    }

    fun createDefaultInstance(quiz_id : Int, type : EnumQuizType) : Quiz
    {
        return QuizHandAction(quiz_id)
    }

    fun loadAllList(context : Context, dbName : String): List<Quiz>
    {
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        var resultList: List<Quiz>? = null
        helper.use {
            resultList = select(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    "id", "type", "update_date")
                    .parseList(
                            rowParser { id: Int, type: String, update_date: String->
                                val q = createQuizFromType(EnumQuizType.fromString(type), id)
                                q.updateDate = update_date
                                q
                            }
                    )
        }
        return resultList!!
    }

    fun getNewQuizId(context : Context, dbName : String) : Int
    {
        Log.e("getNewQuizId", "select max")
        var maxId = -1
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        helper.use {
            val sql = "select max(id) from quiz"
            val cursor = this.rawQuery(sql, null)
            Log.e("getNewQuizId", String.format("cursol = %s", cursor.toString()))
            cursor.use {
                if (it.moveToFirst()) {
                    maxId = it.getInt(0)
                    Log.e("getNewQuizId", "maxId = $maxId")
                }
            }
        }
        return maxId + 1
    }

    fun deleteQuiz(context: Context, dbName: String, quizId : Int) {
        Quiz.delete(context, dbName, quizId)
        return
    }

}
