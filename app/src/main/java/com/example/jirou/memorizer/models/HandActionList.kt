package com.example.jirou.memorizer.models

import com.example.jirou.memorizer.utils.numToChar

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
}
