package com.example.jirou.memorizer

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import com.example.jirou.memorizer.adapters.ListAdapterHandAction
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.models.*

class ResultHandActionActivity : AppCompatActivity() {
    private var mAnsweredHandActionList : HandActionList =  HandActionList()
    private var mCorrectHandActionList : HandActionComparedList =  HandActionComparedList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_hand_action)

        //
        // 問題をload
        //
        val quizId = intent.getIntExtra(INTENT_KEY_QUIZ_ID, -1)
        val quiz = QuizFactory().load(applicationContext, DB_NAME_MEMORIZER, quizId)

        //グリットビューのセル？の作成
        getAnsweredHandActionList(mAnsweredHandActionList)
        mCreateCorrectHandActionList(quiz, mAnsweredHandActionList, mCorrectHandActionList)

        //正解／不正解の判定
        val result : Boolean = mGetResult(mCorrectHandActionList)
        val tRes : TextView = findViewById(R.id.txtResult)
        quiz.score.answerNum++
        if (result) {
            tRes.text = "〇"
            quiz.score.correctNum++
        }
        else {
            tRes.text = "×"
        }
        quiz.save(applicationContext, DB_NAME_MEMORIZER)
        val txtQuestion = findViewById<TextView>(R.id.txtQuiz)
        txtQuestion.text = quiz.toString()

        //グリットビューに各セルの情報を設定
        val gridView : GridView = findViewById(R.id.grdCorrect)
        gridView.adapter = ListAdapterHandAction(applicationContext, gridView, mCorrectHandActionList)

        val btnNext : Button = findViewById(R.id.btnNext)
        btnNext.setOnClickListener( {
            val isNext = true
            mFinishActivity(isNext)
        } )

        val btnRetry : Button = findViewById(R.id.btnRetry)
        btnRetry.setOnClickListener( {
            val isNext = false
            mFinishActivity(isNext)
        } )
    }

    private fun mFinishActivity(isNext: Boolean) {
        // 戻り値を設定
        val intent = Intent()
        intent.putExtra(INTENT_KEY_NEXT_OR_RETRY, isNext)

        // 戻り値を渡して 呼び出し元 の onActivityResult を呼び出す
        setResult(Activity.RESULT_OK, intent)

        // アクティビティを閉じる
        finish()
    }

    private fun mCreateCorrectHandActionList(quiz : Quiz, answeredHandActionList :  HandActionList, correctHandActionList : HandActionComparedList ) {
        correctHandActionList.copyFrom((quiz.correct as CorrectHandAction).handActionList)

        assert(answeredHandActionList.size == correctHandActionList.size)
        for (i in 0 until correctHandActionList.size) {
            (correctHandActionList.get(i) as HandActionCompared).compare(answeredHandActionList.get(i))
        }
    }

    private fun mGetResult(correctHandActionList : HandActionComparedList) : Boolean
    {
        for (i in 0 until correctHandActionList.size) {
            if ((correctHandActionList.get(i) as HandActionCompared).getCompared() != 0) {
                return false
            }
        }
        return true
    }

    private fun getAnsweredHandActionList(answeredHandActionList :  HandActionList) {
        answeredHandActionList.getExtra(intent)
        Log.e("getAnsweredHandActionL", String.format("mHandActionList.size = %d", mAnsweredHandActionList.size))
    }
}