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

class MngEditQzTextActivity : AppCompatActivity() {
    private var mQuiz : QuizText = QuizText(NEW_QUIZ_ID)
    private var mQuestionTextViewList = arrayListOf<TextView>()
    private var mCorrectTextViewList = arrayListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_edit_qz_text)

        val id: Int = intent.getIntExtra(INTENT_KEY_QUIZ_ID, NEW_QUIZ_ID)
        assert(id != NEW_QUIZ_ID)
        mQuiz = QuizFactory().loadOrCreate(applicationContext, DB_NAME_MEMORIZER, id, EnumQuizType.TEXT) as QuizText

        mInitTextViewArray()
        mQuizToTextViews(mQuiz)

        val saveButton : Button = findViewById(R.id.btn_save)
        saveButton.setOnClickListener( {
            // 保存
            mSave(mQuiz)

            // 戻り値を設定
            val intent = Intent()
            intent.putExtra(INTENT_KEY_QUIZ_ID, mQuiz.id)

            // 戻り値を渡して 呼び出し元 の onActivityResult を呼び出す
            setResult(Activity.RESULT_OK, intent)

            // アクティビティを閉じる
            finish()
        }
        )


        val cancelButton : Button = findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener( {
            // 戻り値を設定
            val intent = Intent()
            intent.putExtra(INTENT_KEY_QUIZ_ID, mQuiz.id)

            // 戻り値を渡して 呼び出し元 の onActivityResult を呼び出す
            setResult(Activity.RESULT_CANCELED, intent)

            // アクティビティを閉じる
            finish()
        }
        )
    }

    private fun mSave(quiz : Quiz)
    {
        Log.e("MngEditQzText.mSave", "start")

        // 画面に入力された値を取得
        mTextViewsToQuiz(quiz)

        try {
            // インサート or Update
            quiz.save(applicationContext, DB_NAME_MEMORIZER)
        } catch (e: Exception) {
            Log.e("MngEditQzText.mSave", String.format("exception = [%s]", e.toString()))
            throw e
        }

        Log.e("MngEditQzText.mSave", "end")
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

    private fun mTextViewsToQuiz(quiz: Quiz) {
        val mainText = findViewById<TextView>(R.id.txtQuestionMain)
        (quiz.question as QuestionText).questionMain = mainText.text.toString()
        val tmpQstList = mutableListOf<String>()
        mQuestionTextViewList.forEach {
            if (it.text.toString().isNotBlank()) {
                tmpQstList.add(it.text.toString())
            }
        }
        (quiz.question as QuestionText).questionList = tmpQstList.toList()

        val tmpCrctList = mutableListOf<String>()
        mCorrectTextViewList.forEach {
            if (it.text.toString().isNotBlank()) {
                tmpCrctList.add(it.text.toString())
            }
        }
        (quiz.correct as CorrectText).correctList = tmpCrctList.toList()
    }

}
