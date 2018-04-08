package com.example.jirou.memorizer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.jirou.memorizer.adapters.ListAdapterHandAction
import com.example.jirou.memorizer.models.AV_FOLD_100
import com.example.jirou.memorizer.models.HandAction
import com.example.jirou.memorizer.models.HandActionCorrect
import com.example.jirou.memorizer.utils.numToStr

class MngAddQzHandActionActivity : AppCompatActivity() {
    private var mCorrectHandActionList : ArrayList<HandAction> =  ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_add_qz_hand_action)

        //グリットビューのセル？の作成
        createCorrectHandActionList(mCorrectHandActionList)

        //グリットビューに各セルの情報を設定
        var gridView : GridView = findViewById(R.id.grdEditCorrect)
        gridView.adapter = ListAdapterHandAction(applicationContext, gridView, mCorrectHandActionList)
    }

    private fun createCorrectHandActionList(correctHandActionList :  ArrayList<HandAction> ) {
        for (i in 14 downTo 2) {
            for (j in 14 downTo 2) {
                //配列にハンド名、アクションを格納
                correctHandActionList.add(HandActionCorrect(numToStr(i) + numToStr(j), AV_FOLD_100))
            }
        }

        correctHandActionList[0].setActionVal(100)
        correctHandActionList[1].setActionVal(50)
    }

}
