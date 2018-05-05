package com.example.jirou.memorizer.models

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteException
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
        var resultPair : Pair<Int, String>? = null
        helper.use {
            resultPair = select(MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                    "id", "type")
                    .whereArgs("id = {id}", "id" to quiz_id.toString())
                    .parseSingle(
                            rowParser { id: Int, type: String->
                                Pair(id, type)
                            }
                    )

        }
        val id = resultPair!!.first
        val type = resultPair!!.second
        val quiz : Quiz
        when (type) {
            EnumQuizType.HAND_ACTION.toString() ->
            {
                quiz = QuizHandAction(id)
            }
            else ->
            {
                throw Exception(String.format("type %s is Invalid", type))
            }
        }

        quiz.question.load(context, dbName)
        quiz.correct.load(context, dbName)

        return quiz
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
}
