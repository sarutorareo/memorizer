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
    private val mHandActionList = HandActionList()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_starting_hand)

        //
        //グリットビューに各セルの情報を設定
        //
        val gridView : GridView = findViewById(R.id.grdInputStartHand)
        gridView.adapter = ListAdapterHandAction(applicationContext, gridView, mHandActionList)

        //
        //OnTouchイベントに対する設定
        //
        gridView.setOnTouchListener((gridView.adapter as ListAdapterHandAction).getOnTouchListener(this::getActionValue))

        //
        // Answerボタンクリックイベントに対する設定
        //
        val btnAnswer : Button = findViewById(R.id.btnAnswer)
        btnAnswer.setOnClickListener( {
            val intent = Intent(application, ResultActivity::class.java)
            intent.putExtra(HAND_ACTION_ARRAY_SIZE, mHandActionList.size)
            Log.e("setOnClickListener", String.format("put mHandActionList.size = %d",  mHandActionList.size))

            for (i in 0 until mHandActionList.size) {
                intent.putExtra(String.format(HAND_ACTION_ARRAY_FMT, i), mHandActionList.get(i))
            }
            startActivity(intent)
        } )
    }

    private fun getActionValue() : Int
    {
        val rdg : RadioGroup = findViewById(R.id.rdgAction)
        val checkedId : Int = rdg.checkedRadioButtonId
        Log.e("getActionValue", String.format("checked is %d", checkedId))
        val button : RadioButton = findViewById(checkedId) ?: return -1

        val tag : String = button.tag.toString()
        Log.e("getActionValue", String.format("tag is %s", tag))

        return if (tag == "null") {
            -1
        }
        else
        {
            tag.toInt()
        }
    }

}
