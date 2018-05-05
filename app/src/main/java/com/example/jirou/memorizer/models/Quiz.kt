package com.example.jirou.memorizer.models

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import org.jetbrains.anko.db.insertOrThrow
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction

abstract class Quiz()  {
    private var mId : Int = 0
    protected var mQuestion : Question = mCreateQuestion(mId)
    protected var mCorrect : Correct = mCreateCorrect(mId)

    init {
    }

    companion object {
    }

    constructor(id : Int) : this() {
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
                insertOrThrow(
                        MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                        *MemorizeDBOpenHelper.addCreateUpdateDate(arrayOf("id" to id.toString(),
                                "type" to mGetTypeStr())
                        )
                )

                correct.save(context, dbName)
            }
        }
    }

    protected var mIsNewRecord = true
    val isNewRecord : Boolean
        get() = mIsNewRecord

}