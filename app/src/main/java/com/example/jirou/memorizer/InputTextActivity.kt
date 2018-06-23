package com.example.jirou.memorizer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.models.*
import org.jetbrains.anko.collections.forEachWithIndex

class InputTextActivity : AppCompatActivity() {
    private var mQuiz = QuizText(NEW_QUIZ_ID)
    private var mQuestionTextViewList = arrayListOf<TextView>()
    private var mAnswerTextViewList = arrayListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_text)

        val id: Int = intent.getIntExtra(INTENT_KEY_QUIZ_ID, NEW_QUIZ_ID)
        assert(id != NEW_QUIZ_ID)
        mQuiz = QuizFactory().loadOrCreate(applicationContext, DB_NAME_MEMORIZER, id, EnumQuizType.TEXT) as QuizText

        mInitTextViewArray()
        mQuizToTextViews(mQuiz)
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

    private fun mTextViewsToQuiz(quiz: Quiz): List<String> {
        val tmpAnsList = mutableListOf<String>()
        mAnswerTextViewList.forEach {
            if (it.text.toString().isNotBlank()) {
                tmpAnsList.add(it.text.toString())
            }
        }
        return tmpAnsList.toList()
    }

}
