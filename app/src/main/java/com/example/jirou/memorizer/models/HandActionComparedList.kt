package com.example.jirou.memorizer.models

import com.example.jirou.memorizer.utils.numToStr

open class HandActionComparedList : HandActionList()  {
    init
    {
        mHandActionList.clear() // 親が作ったものを破棄
        for (i in 14 downTo 2) {
            for (j in 14 downTo 2) {
                //配列にハンド名、アクションを格納
                mHandActionList.add(HandActionCompared(numToStr(i) + numToStr(j), AV_FOLD_100))
            }
        }
    }

    fun getCorrect( idx : Int) : HandActionCompared{
        return get(idx) as HandActionCompared
    }
}
