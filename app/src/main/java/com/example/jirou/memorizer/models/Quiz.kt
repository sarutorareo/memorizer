package com.example.jirou.memorizer.models

import android.content.Context
import android.util.Log
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import org.jetbrains.anko.db.*

abstract class Quiz(id : Int)   {
    private var mId : Int = 0
    protected var mQuestion : Question = mCreateQuestion(mId)
    protected var mCorrect : Correct = mCreateCorrect(mId)

    init {
        mId = id
        mQuestion = mCreateQuestion(id)
        mCorrect = mCreateCorrect(id)
    }

    val id : Int
        get() {
            return mId
        }

    val question : Question
        get() {
            return mQuestion
        }

    val correct : Correct
        get() {
            return mCorrect
        }

    protected abstract fun mCreateQuestion(id : Int) : Question
    protected abstract fun mCreateCorrect(id : Int) : Correct

    protected abstract fun mGetTypeStr() : String

    fun save(context : Context, dbName: String)
    {
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        helper.use {
            transaction {
                Log.d("Quiz.save", String.format("save id[%d], type[%s]", id, mGetTypeStr() ))

                replaceOrThrow(
                        MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                        *MemorizeDBOpenHelper.addUpdateDate(arrayOf("id" to id.toString(),
                                "type" to mGetTypeStr())
                        )
                )

                question.save(context, dbName)
                correct.save(context, dbName)
            }
        }
    }
}
