package com.example.jirou.memorizer

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.models.*

class MngEditQzTextActivity : AppCompatActivity() {
    private var mQuiz : QuizText = QuizText(NEW_QUIZ_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_edit_qz_text)

        val id: Int = intent.getIntExtra(INTENT_KEY_QUIZ_ID, NEW_QUIZ_ID)
        assert(id != NEW_QUIZ_ID)
        mQuiz = QuizFactory().loadOrCreate(applicationContext, DB_NAME_MEMORIZER, id, EnumQuizType.TEXT) as QuizText

        val mainText = findViewById<TextView>(R.id.txtQuestionMain)
        mainText.text = String.format("%s %s %s %s",
                mQuiz.type.toString())

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
    }

    private fun mSave(quiz : Quiz)
    {
        ;
    }
}
