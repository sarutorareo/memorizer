package com.example.jirou.memorizer.models

import android.content.Intent
import android.util.Log
import com.example.jirou.memorizer.utils.numToChar
import org.jetbrains.anko.collections.forEachWithIndex

const val INTENT_KEY_HAND_ACTION_ARRAY_FMT : String = "HandActionArray_%d"
const val INTENT_KEY_HAND_ACTION_ARRAY_SIZE : String = "HandActionArraySize"

open class HandActionList  {
    protected val mHandActionList : ArrayList<HandAction> =  ArrayList()

    init {
        for (i in 14 downTo 2) {
            for (j in 14 downTo 2) {
                //配列にハンド名、アクションを格納
                mHandActionList.add(HandAction(numToChar(i).toString() + numToChar(j).toString(), AV_FOLD_100))
            }
        }
    }

    val size : Int
      get() {
          return mHandActionList.size
      }

    fun get(idx : Int) : HandAction
    {
        assert((idx < mHandActionList.size) &&  (idx >= 0))
        return mHandActionList[idx]
    }
    fun getFromHand(hand : String) : HandAction
    {
        mHandActionList.forEach() {
            if (it.hand == hand)
                return it
        }
        throw Exception ("hand (%s) is not exist")
    }

    val list : ArrayList<HandAction>
        get() = mHandActionList

    fun copyHandActionFrom(srcHa : HandAction)
    {
        val hand = getFromHand(srcHa.hand)
        hand.copyFrom(srcHa)
    }

    fun copyFrom(list : HandActionList)
    {
        list.list.forEach {
            copyHandActionFrom(it)
        }
    }

    fun putExtra(intent : Intent) {
        // 回答をput
        intent.putExtra(INTENT_KEY_HAND_ACTION_ARRAY_SIZE, mHandActionList.size)
        mHandActionList.forEachWithIndex { i, handAction ->
            intent.putExtra(String.format(INTENT_KEY_HAND_ACTION_ARRAY_FMT, i), handAction)
        }
    }

    fun getExtra(intent : Intent) {
        // 配列は今のところダメ　個別にHandActionを渡すのはできた
        val haArraySize: Int = intent.getIntExtra(INTENT_KEY_HAND_ACTION_ARRAY_SIZE, 0)
        for (i in 0 until haArraySize) {
            val ha: HandAction = intent.getParcelableExtra(String.format(INTENT_KEY_HAND_ACTION_ARRAY_FMT, i))
            mHandActionList[i].copyFrom(ha)
        }
    }
}
