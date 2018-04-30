package com.example.jirou.memorizer.models

import android.content.Context
import android.util.Log
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import org.jetbrains.anko.db.insertOrThrow
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction

open class CorrectHandAction(quizId: Int) : Correct(quizId)  {
    init {
    }
    private val mHandActionList = HandActionList()
    val handActionList : HandActionList
        get () {
            return mHandActionList
        }

    override fun save(context : Context, dbName: String)
    {
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        helper.use {
            transaction {
                handActionList.list.forEach {
                    insertOrThrow(
                            MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION_ITEM,
                            *MemorizeDBOpenHelper.addCreateUpdateDate(arrayOf("quiz_id" to quizId.toString(),
                                    "hand" to it.hand,
                                    "action_val" to it.actionVal.toString()))
                    )
                }

            }
        }
    }

    override fun load(context : Context, dbName: String)
    {
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        helper.use {
            val resultList = select(MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION_ITEM,
                    "hand", "action_val")
                    .whereArgs("quiz_id = {quizId}", "quizId" to quizId.toString())
                    .parseList(
                        rowParser { hand: String, action_val: Int->
                            HandAction(hand, action_val)
                        }
                    )
            resultList.forEach {
                handActionList.copyFrom(it)
            }
        }
    }

}
