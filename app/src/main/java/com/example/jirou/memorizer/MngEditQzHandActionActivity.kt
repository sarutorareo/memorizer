package com.example.jirou.memorizer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.jirou.memorizer.adapters.ListAdapterHandAction
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.models.*

const val REQUEST_CODE_EDIT_HAND_ACTION = 1000
const val INTENT_KEY_QUIZ_ID = "quiz_id"

class MngEditQzHandActionActivity : AppCompatActivity() {
    private var mQuiz : QuizHandAction = QuizHandAction(-1)

    init {
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_edit_qz_hand_action)

        val id: Int = intent.getIntExtra(INTENT_KEY_QUIZ_ID, -1)
        mQuiz = QuizFactory().loadOrCreate(applicationContext, DB_NAME_MEMORIZER, id, EnumQuizType.HAND_ACTION) as QuizHandAction

        findViewById<TextView>(R.id.txtQuizID).text = String.format("quiz_id = %d", mQuiz.id)
        //
        //グリットビューのセル？の作成
        //
        mCreateHandActionList((mQuiz.correct as CorrectHandAction).handActionList)

        //
        //グリットビューに各セルの情報を設定
        //
        val gridView : GridView = findViewById<GridView>(R.id.grdEditCorrect) as GridView
        gridView.adapter = ListAdapterHandAction(applicationContext, gridView, (mQuiz.correct as CorrectHandAction).handActionList)

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

        val loadButton : Button = findViewById(R.id.btnLoadQzHandAction)
        loadButton.setOnClickListener( {
            // 渡す値を設定
            val intent = Intent()
            intent.putExtra(INTENT_KEY_QUIZ_ID, mQuiz.id)

            // 情報を渡して MainActivity の onActivityResult を呼び出す
            setResult(Activity.RESULT_OK, intent)

            // アクティビティを閉じる
            finish()
        }
        )
    }

    private fun mSaveHandAction(quiz : Quiz)
    {
        Log.e("mSaveHandAction", "start")

        try {
            // インサート or Update
            // Quizの単位で新規 or 既存を管理して Inser or Updateを分ける
            // 新規かどうかはid is nullで表す
            // 採番はプログラム（画面から入力可）でやる
            quiz.save(applicationContext, DB_NAME_MEMORIZER)
        } catch (e: Exception) {
            Log.e("mSaveHandAction", String.format("exception = [%s]", e.toString()))
            throw e
        }

        Log.e("mSaveHandAction", "end")
    }
/*
    private fun mLoadHandAction(quiz : Quiz)
    {
        Log.e("mLoadHandAction", "start")

        try {
            quiz = QuizFactory().load(applicationContext, DB_NAME_MEMORIZER, quiz.id)
        } catch (e: Exception) {
            Log.e("mLoadHandAction", String.format("exception = [%s]", e.toString()))
            throw e
        }

        Log.e("mLoadHandAction", "end")
    }
    */

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
