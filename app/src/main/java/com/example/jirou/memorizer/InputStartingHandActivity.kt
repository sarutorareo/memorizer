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
        mHandActionList.getExtra(intent)
        val gridView : GridView = findViewById(R.id.grdInputStartHand)
        gridView.adapter = ListAdapterHandAction(applicationContext, gridView, mHandActionList)

        //
        //OnTouchイベントに対する設定
        //
        gridView.setOnTouchListener((gridView.adapter as ListAdapterHandAction).getOnTouchListener(this::mGetActionValue))

        //
        // Answerボタンクリックイベントに対する設定
        //
        val btnAnswer : Button = findViewById(R.id.btnAnswer)
        btnAnswer.setOnClickListener( {
            val intent = Intent(application, ResultActivity::class.java)

            // quizIdをput
            intent.putExtra(INTENT_KEY_QUIZ_ID, quizId)

            // 回答をput
            mHandActionList.putExtra(intent)

            startActivityForResult(intent, EnumRequestCodes.TRAINING.rawValue)
        } )
    }

    private fun mGetActionValue() : Int
    {
        val rdg : RadioGroup = findViewById(R.id.rdgAction)
        val checkedId : Int = rdg.checkedRadioButtonId
        val button : RadioButton = findViewById(checkedId) ?: return AV_FOLD_100
        val tag : String = button.tag.toString()

        // 0 - 99 : raise or fold (0 : fold, 99 : raise)
        // 100 - 200 : raise or call ( 100 : call, 200 : raise)
        return if (tag == "null") {
            AV_FOLD_100
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
            if (!isNext) {
                mHandActionList.putExtra(intent)
            }

            // 戻り値を渡して 呼び出し元 の onActivityResult を呼び出す
            setResult(Activity.RESULT_OK, intent)

            // アクティビティを閉じる
            finish()
        }
    }

}
