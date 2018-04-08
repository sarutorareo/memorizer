package com.example.jirou.memorizer

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.example.jirou.memorizer.utils.*
import com.example.jirou.memorizer.models.*
import com.example.jirou.memorizer.adapters.ListAdapterHandAction

const val HAND_ACTION_ARRAY_FMT : String = "HandActionArray_%d"
const val HAND_ACTION_ARRAY_SIZE : String = "HandActionArraySize"
class InputStartingHandActivity : AppCompatActivity() {
    private val mHandActionList = ArrayList<HandAction>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_starting_hand)

        //グリットビューのセル？の作成
        for (i in 14 downTo 2) {
            for (j in 14 downTo 2) {
                //配列にタイトルと画像を格納
                mHandActionList.add(HandAction(numToStr(i)+numToStr(j), AV_FOLD_100))
            }
        }

        val gridView : GridView = findViewById(R.id.grdInputStartHand)
        //グリットビューに各セルの情報を設定
        gridView.adapter = ListAdapterHandAction(applicationContext, gridView, mHandActionList)

        // Implement On Touch listener
        gridView.setOnTouchListener { v, event ->
            val currAction : String
            ////////////////////////////////////////////////////////////
            // イベントの状態を調べる
            val action = event.action and MotionEvent.ACTION_MASK
            currAction = when (action) {
                MotionEvent.ACTION_DOWN -> "DOWN"
                MotionEvent.ACTION_MOVE -> "MOVE"
                MotionEvent.ACTION_UP -> "UP"
                MotionEvent.ACTION_CANCEL -> "CANCEL"
                else -> "null"
            }

            val position = (gridView.adapter as ListAdapterHandAction).axisToPosition(gridView.numColumns, event.x, event.y)
            Log.e("onTouch", String.format("action is %s (x=%f, y=%f)", currAction, event.x, event.y) )
            Log.e("onTouch", String.format("(col=%d, row=%d, position=%d) gridView.width = %d, gridView.height = %d, gridView.numColumns = %d",
                    (gridView.adapter as ListAdapterHandAction).xToCol(event.x),
                    (gridView.adapter as ListAdapterHandAction).yToRow(event.y),
                    position, gridView.width, gridView.height, gridView.numColumns)
            )

            Log.e("onTouch", String.format("oGridList.size = %d", mHandActionList.size))
            if (((action == MotionEvent.ACTION_DOWN) || (action == MotionEvent.ACTION_MOVE))
                    && (position >= 0) && (position < mHandActionList.size)) {
                //配列から、アイテムを取得
                val handAction = mHandActionList[position]
                Log.e("onTouch", "oGrid is not null")
                handAction.setActionVal(getActionValue())
                // getViewで対象のViewを更新
                val targetView : View? = gridView.getChildAt(position)
                if (targetView != null) {
                    Log.e("onTouch", "targetView is not null")
                    gridView.adapter.getView(position, targetView, gridView)
                } else {
                    Log.e("onTouch", "targetView is null")
                }
            }

            false
        }

        val btnAnswer : Button = findViewById(R.id.btnAnswer)
        btnAnswer.setOnClickListener( {
            val intent = Intent(application, ResultActivity::class.java)
//            intent.putParcelableArrayListExtra(HAND_ACTION_ARRAY, mHandActionList)
            intent.putExtra(HAND_ACTION_ARRAY_SIZE, mHandActionList.size)
            Log.e("setOnClickListener", String.format("put mHandActionList.size = %d",  mHandActionList.size))

            for (i in 0 until mHandActionList.size) {
                intent.putExtra(String.format(HAND_ACTION_ARRAY_FMT, i), mHandActionList[i])
            }
            startActivity(intent)
        } )
    }

    private fun getActionValue() : Int
    {
        val rdg : RadioGroup = findViewById(R.id.rdgAction)
        val checked : Int = rdg.checkedRadioButtonId
        Log.e("getActionValue", String.format("checked is %d", checked))
        val button : RadioButton = findViewById(checked) ?: return -1

        val tag : String = button.tag.toString()
        Log.e("getActionValue", String.format("tag is %s", tag.toString() ))

        return if (tag == "null") {
            -1
        }
        else
        {
            tag.toInt()
        }
    }

}
