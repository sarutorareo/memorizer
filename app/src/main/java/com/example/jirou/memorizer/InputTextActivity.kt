package com.example.jirou.memorizer

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.models.*
import org.jetbrains.anko.collections.forEachWithIndex

const val INTENT_KEY_ANS_FMT : String = "INTENT_KEY_ANS_%d"
class InputTextActivity : AppCompatActivity() {
    private var mQuiz = QuizText(NEW_QUIZ_ID)
    private var mQuestionTextViewList = arrayListOf<TextView>()
    private var mAnswerTextViewList = arrayListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_text)

        val quizId: Int = intent.getIntExtra(INTENT_KEY_QUIZ_ID, NEW_QUIZ_ID)
        assert(quizId != NEW_QUIZ_ID)
        mQuiz = QuizFactory().loadOrCreate(applicationContext, DB_NAME_MEMORIZER, quizId, EnumQuizType.TEXT) as QuizText
        val txtQuiz = findViewById<TextView>(R.id.txtQuiz)
        txtQuiz.text = mQuiz.toString()

        mInitTextViewArray()
        mQuizToTextViews(mQuiz)
        mGetAnswerList(intent)

        //
        // Answerボタンクリックイベントに対する設定
        //
        val btnAnswer : Button = findViewById(R.id.btnAnswer)
        btnAnswer.setOnClickListener( {
            val intent = Intent(application, ResultTextActivity::class.java)

            // quizIdをput
            intent.putExtra(INTENT_KEY_QUIZ_ID, quizId)

            // 回答をput
            mPutAnswerList(intent)

            startActivityForResult(intent, EnumRequestCodes.TRAINING_TEXT.rawValue)
        } )
    }

    private fun mPutAnswerList(intent : Intent) {
        mAnswerTextViewList.forEachWithIndex { i, textView ->
            Log.e("mPutAnswerList", String.format("i = %d, str = %s", i, textView.text))
            intent.putExtra(String.format(INTENT_KEY_ANS_FMT, i), textView.text.toString())
        }
    }

    private fun mGetAnswerList(intent : Intent) {
        mAnswerTextViewList.forEachWithIndex { i, textView ->
            textView.text = intent.getStringExtra(String.format(INTENT_KEY_ANS_FMT, i))
        }
    }

    private fun mInitTextViewArray() {
        mQuestionTextViewList = arrayListOf(
                findViewById(R.id.txtQst1),
                findViewById(R.id.txtQst2),
                findViewById(R.id.txtQst3),
                findViewById(R.id.txtQst4),
                findViewById(R.id.txtQst5))
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EnumRequestCodes.TRAINING_TEXT.rawValue) {
            // Nextで終わったか、Retryで終わったか を取得する
            val received = data!!
            val isNext = received.getBooleanExtra(INTENT_KEY_NEXT_OR_RETRY, true)

            // 戻り値を設定
            val intent = Intent()
            intent.putExtra(INTENT_KEY_NEXT_OR_RETRY, isNext)
            if (!isNext) {
                mPutAnswerList(intent)
            }

            // 戻り値を渡して 呼び出し元 の onActivityResult を呼び出す
            setResult(Activity.RESULT_OK, intent)

            // アクティビティを閉じる
            finish()
        }
    }

}
