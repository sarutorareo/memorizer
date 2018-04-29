package com.example.jirou.memorizer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.jirou.memorizer.models.HandAction
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import com.example.jirou.memorizer.adapters.ListAdapterHandAction
import com.example.jirou.memorizer.models.HandActionCompared
import com.example.jirou.memorizer.models.HandActionList
import com.example.jirou.memorizer.models.HandActionComparedList

class ResultActivity : AppCompatActivity() {
    private var mAnsweredHandActionList : HandActionList =  HandActionList()
    private var mCorrectHandActionList : HandActionComparedList =  HandActionComparedList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        //グリットビューのセル？の作成
        getAnsweredHandActionList(mAnsweredHandActionList)
        createCorrectHandActionList(mAnsweredHandActionList, mCorrectHandActionList)

        //正解／不正解の判定
        val result : Boolean = getResult(mCorrectHandActionList)
        val tRes : TextView = findViewById(R.id.txtResult)
        tRes.text = when (result) { true -> "〇" else -> "×" }

        //グリットビューに各セルの情報を設定
        var gridView : GridView = findViewById(R.id.grdCorrect)
        gridView.adapter = ListAdapterHandAction(applicationContext, gridView, mCorrectHandActionList)

        //グリットビューに各セルの情報を設定
        gridView = findViewById(R.id.grdAnswered)
        gridView.adapter = ListAdapterHandAction(applicationContext, gridView, mAnsweredHandActionList)

        val btnAnswer : Button = findViewById(R.id.btnRetry)
        btnAnswer.setOnClickListener( {
            finish()
        } )

    }

    private fun createCorrectHandActionList(answeredHandActionList :  HandActionList, correctHandActionList : HandActionComparedList ) {
        correctHandActionList.get(0).setActionVal(100)
        correctHandActionList.get(1).setActionVal(50)

        assert(answeredHandActionList.size == correctHandActionList.size)
        for (i in 0 until correctHandActionList.size) {
            (correctHandActionList.get(i) as HandActionCompared).compare(answeredHandActionList.get(i) as HandAction)
        }
    }

    private fun getResult(correctHandActionList : HandActionComparedList) : Boolean
    {
        for (i in 0 until correctHandActionList.size) {
            if ((correctHandActionList.get(i) as HandActionCompared).getCompared() != 0) {
                return false
            }
        }
        return true
    }

    private fun getAnsweredHandActionList(answeredHandActionList :  HandActionList) {

        // 配列は今のところダメ　個別にHandActionを渡すのはできた
        val haArraySize: Int = intent.getIntExtra(HAND_ACTION_ARRAY_SIZE, 0)
        val tv: TextView = findViewById(R.id.multiAutoCompleteTextView)
        for (i in 0 until haArraySize) {
            val ha: HandAction = intent.getParcelableExtra(String.format(HAND_ACTION_ARRAY_FMT, i))
            answeredHandActionList.get(i).copyFrom(ha)

            var logStr: String = String.format("handAction  (%s, %d)", ha.getHand(), ha.getActionVal())
            Log.e("ResultActivity.onCreate", String.format("ResultActivity.onCreate %s", logStr))
            tv.text = String.format("%s %s", tv.text.toString(), logStr)
        }

        Log.e("ResultActivity.onCreate", String.format("mHandActionList.size = %d", mAnsweredHandActionList.size))
    }
}
