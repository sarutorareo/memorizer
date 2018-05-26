package com.example.jirou.memorizer.models

import android.content.Context
import android.util.Log
import com.example.jirou.memorizer.NEW_QUIZ_ID
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import junit.framework.Assert
import org.jetbrains.anko.db.*

abstract class Quiz(id : Int)   {
    private var mId : Int = 0
    protected var mQuestion : Question = mCreateQuestion(mId)
    protected var mCorrect : Correct = mCreateCorrect(mId)
    private var mUpdateDate : String = ""

    companion object {
        fun delete(context: Context, dbName: String, id: Int) {
            Log.e("Quiz.delete", "start delete")
            val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
            helper.use {
                transaction {
                    val res = delete(MemorizeDBOpenHelper.TABLE_NAME_QUIZ, "id = ?", arrayOf(id.toString()))
                    assert(res == 1)
                }
            }
        }
    }

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
        Log.e("Quiz.save", "start save")
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

    abstract val type : EnumQuizType
    abstract val title : String

    var updateDate : String
        get() = mUpdateDate
        set(v : String) {
            mUpdateDate = v
        }
}
