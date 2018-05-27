package com.example.jirou.memorizer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.jirou.memorizer.models.*
import com.example.jirou.memorizer.adapters.ListAdapterHandAction
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER

const val INTENT_KEY_HAND_ACTION_ARRAY_FMT : String = "HandActionArray_%d"
const val INTENT_KEY_HAND_ACTION_ARRAY_SIZE : String = "HandActionArraySize"
class InputStartingHandActivity : AppCompatActivity() {
    private val mHandActionList = HandActionList()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_starting_hand)

        //
        // 問題をload
        //
        val quizId = intent.getIntExtra(INTENT_KEY_QUIZ_ID, -1)
        val quiz = QuizFactory().load(applicationContext, DB_NAME_MEMORIZER, quizId)
        val txtQuestion = findViewById<TextView>(R.id.txtQuestion)
        txtQuestion.text = quiz.question.toString()

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

            // quizIdをput
            intent.putExtra(INTENT_KEY_QUIZ_ID, quizId)

            // 回答をput
            intent.putExtra(INTENT_KEY_HAND_ACTION_ARRAY_SIZE, mHandActionList.size)
            Log.e("setOnClickListener", String.format("put mHandActionList.size = %d",  mHandActionList.size))
            for (i in 0 until mHandActionList.size) {
                intent.putExtra(String.format(INTENT_KEY_HAND_ACTION_ARRAY_FMT, i), mHandActionList.get(i))
            }

            startActivityForResult(intent, EnumRequestCodes.TRAINING.rawValue)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EnumRequestCodes.TRAINING.rawValue) {
            // Nextで終わったか、Retryで終わったか を取得する
            val received = data!!
            val isNext = received.getBooleanExtra(INTENT_KEY_NEXT_OR_RETRY, true)

            // 戻り値を設定
            val intent = Intent()
            intent.putExtra(INTENT_KEY_NEXT_OR_RETRY, isNext)

            // 戻り値を渡して 呼び出し元 の onActivityResult を呼び出す
            setResult(Activity.RESULT_OK, intent)

            // アクティビティを閉じる
            finish()
        }
    }

}
