package com.example.jirou.memorizer

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.jirou.memorizer.adapters.ListAdapterHandAction
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import com.example.jirou.memorizer.models.HandActionList
import com.example.jirou.memorizer.models.Quiz
import com.example.jirou.memorizer.models.QuizHandAction
import org.jetbrains.anko.db.insertOrThrow

class MngEditQzHandActionActivity : AppCompatActivity() {
    private var mQuiz : QuizHandAction = QuizHandAction(0)
    private var mHandActionList : HandActionList =  HandActionList()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_edit_qz_hand_action)

        //
        //グリットビューのセル？の作成
        //
        mCreateHandActionList(mHandActionList)

        //
        //グリットビューに各セルの情報を設定
        //
        val gridView : GridView = findViewById<GridView>(R.id.grdEditCorrect) as GridView
        gridView.adapter = ListAdapterHandAction(applicationContext, gridView, mHandActionList)

        //
        //OnTouchイベントに対する設定
        //
        gridView.setOnTouchListener((gridView.adapter as ListAdapterHandAction).getOnTouchListener(this::mGetActionValue))

        //
        //SaveButtonイベントに対する設定
        //
        val saveButton : Button = findViewById(R.id.btnSaveQzHandAction)
        saveButton.setOnClickListener( {
            // HandActionのマトリクスを保存する
            mSaveHandAction(mQuiz)
        }
        )
    }

    private fun mSaveHandAction(quiz : Quiz)
    {
        Log.e("mSaveHandAction", "start")

        try {
            val helper = MemorizeDBOpenHelper.getInstance(applicationContext, DB_NAME_MEMORIZER)
            helper.use {
//                handActionList.save(this)
            }
        } catch (e: Exception) {
            Log.e("mSaveHandAction", String.format("exception = [%s]", e.toString()))
            throw e
        }
        Log.e("mSaveHandAction", "end")
    }

    private fun mCreateHandActionList(handActionList :  HandActionList ) {
        handActionList.get(0).setActionVal(100)
        handActionList.get(1).setActionVal(50)
    }

    private fun mGetActionValue() : Int
    {
        val rdg : RadioGroup = findViewById(R.id.rdgEditAction)
        val checkedId : Int = rdg.checkedRadioButtonId
        val button : RadioButton = findViewById(checkedId) ?: return -1
        val tag : String = button.tag.toString()

        return if (tag == "null") {
            -1
        }
        else
        {
            tag.toInt()
        }
    }

}
