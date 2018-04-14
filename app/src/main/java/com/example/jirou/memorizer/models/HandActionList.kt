package com.example.jirou.memorizer.models

import com.example.jirou.memorizer.utils.numToStr

open class HandActionList  {
    protected val mHandActionList : ArrayList<HandAction> =  ArrayList()

    init {
        for (i in 14 downTo 2) {
            for (j in 14 downTo 2) {
                //配列にハンド名、アクションを格納
//                mHandActionList.add(mCreateNewHandAction(i, j))
                mHandActionList.add(HandAction(numToStr(i) + numToStr(j), AV_FOLD_100))
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
}
