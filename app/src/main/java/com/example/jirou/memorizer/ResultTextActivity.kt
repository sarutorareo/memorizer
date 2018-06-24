package com.example.jirou.memorizer

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.models.*
import org.jetbrains.anko.collections.forEachWithIndex

class ResultTextActivity : AppCompatActivity() {
    private var mQuiz : QuizText = QuizText(NEW_QUIZ_ID)
    private var mQuestionTextViewList = arrayListOf<TextView>()
    private var mCorrectTextViewList = arrayListOf<TextView>()
    private var mAnswerTextViewList = arrayListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_text)

        val id: Int = intent.getIntExtra(INTENT_KEY_QUIZ_ID, NEW_QUIZ_ID)
        assert(id != NEW_QUIZ_ID)
        mQuiz = QuizFactory().loadOrCreate(applicationContext, DB_NAME_MEMORIZER, id, EnumQuizType.TEXT) as QuizText

        mInitTextViewArray()
        mQuizToTextViews(mQuiz)
        mAnswerToTextViews(intent)
        //正解／不正解の判定
        val result : Boolean = mGetResult()
        val tRes : TextView = findViewById(R.id.txtResult)
        mQuiz.score.answerNum++
        if (result) {
            tRes.text = "〇"
            mQuiz.score.correctNum++
        }
        else {
            tRes.text = "×"
        }
        mQuiz.save(applicationContext, DB_NAME_MEMORIZER)
        val tQuiz : TextView = findViewById(R.id.txtQuiz)
        tQuiz.text = mQuiz.toString()

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

    private fun mInitTextViewArray() {
        mQuestionTextViewList = arrayListOf(
                findViewById(R.id.txtQst1),
                findViewById(R.id.txtQst2),
                findViewById(R.id.txtQst3),
                findViewById(R.id.txtQst4),
                findViewById(R.id.txtQst5))
        mCorrectTextViewList = arrayListOf(
                findViewById(R.id.txtCrct1),
                findViewById(R.id.txtCrct2),
                findViewById(R.id.txtCrct3),
                findViewById(R.id.txtCrct4),
                findViewById(R.id.txtCrct5))
        mAnswerTextViewList = arrayListOf(
                findViewById(R.id.txtAns1),
                findViewById(R.id.txtAns2),
                findViewById(R.id.txtAns3),
                findViewById(R.id.txtAns4),
                findViewById(R.id.txtAns5))
    }

    private fun mQuizToTextViews(quiz: Quiz)
    {
        val mainText = findViewById<TextView>(R.id.txtQuestionMain)
        mainText.text = (quiz.question as QuestionText).questionMain

        (quiz.question as QuestionText).questionList.forEachWithIndex { i, s ->
            mQuestionTextViewList[i].text = s
        }

        (quiz.correct as CorrectText).correctList.forEachWithIndex { i, s ->
            mCorrectTextViewList[i].text =  s
        }
    }

    private fun mAnswerToTextViews(intent: Intent)
    {
        mAnswerTextViewList.forEachWithIndex { i, textView ->
            textView.text = intent.getStringExtra(String.format(INTENT_KEY_ANS_FMT, i))
        }
    }

    private fun mGetResult() : Boolean {
        var result = true
        mAnswerTextViewList.forEachWithIndex { i, ans ->
            val crct = mCorrectTextViewList[i]
            if (ans.text != crct.text) {
                Log.e("mGetResult",
                        String.format("i = %d, ans.text = %s, crct.text = %s",
                                i, ans.text, crct.text))
                ans.setTextColor(Color.argb(255, 255, 0, 0))
                result = false
            }
        }
        return result
    }
}
