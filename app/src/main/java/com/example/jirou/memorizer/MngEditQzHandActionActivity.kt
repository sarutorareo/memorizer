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
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import com.example.jirou.memorizer.models.HandActionList
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.insertOrThrow

class MngEditQzHandActionActivity : AppCompatActivity() {
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
            mSaveHandAction(mHandActionList)
        }
        )
    }

    private fun mSaveHandAction(handActionList : HandActionList)
    {
        Log.e("mSaveHandAction", "start")

        try {
            val helper = MemorizeDBOpenHelper.getInstance(applicationContext)
            helper.use {
                Log.e("mSaveHandAction", "before Insert")
                insertOrThrow(
                        MemorizeDBOpenHelper.TABLE_NAME_TEST,
                        *MemorizeDBOpenHelper.addCreateUpdateDate(arrayOf("bmi" to "bmi_val", "date" to "1900/1/1"))
                )
                insertOrThrow(
                        MemorizeDBOpenHelper.TABLE_NAME_QUIZ,
                        *MemorizeDBOpenHelper.addCreateUpdateDate(arrayOf("type" to "test_type"))
                )
                Log.e("mSaveHandAction", "after Insert")
            }
        } catch (e: Exception) {
            Log.e("mSaveHandAction", String.format("exception = [%s]", e.toString()))
        }

        // IDを採番
        val id : Int = 0 //HandActionList.getNewID()

        // 保存

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
