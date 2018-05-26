package com.example.jirou.memorizer.models

import android.content.Context
import android.util.Log
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper

import org.jetbrains.anko.db.replaceOrThrow
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
        Log.e("CorrectHandAction.save", "start save")

        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        helper.use {
            transaction {
                handActionList.list.forEach {
                    replaceOrThrow (
                            MemorizeDBOpenHelper.TABLE_NAME_CRCT_HAND_ACTION_ITEM,
                            *MemorizeDBOpenHelper.addUpdateDate(arrayOf("quiz_id" to quizId.toString(),
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
            val resultList = select(MemorizeDBOpenHelper.TABLE_NAME_CRCT_HAND_ACTION_ITEM,
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
