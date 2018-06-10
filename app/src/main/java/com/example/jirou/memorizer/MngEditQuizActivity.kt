package com.example.jirou.memorizer

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.models.EnumQuizType
import com.example.jirou.memorizer.models.EnumRequestCodes
import com.example.jirou.memorizer.models.QuizFactory
import org.jetbrains.anko.collections.forEachWithIndex
import kotlinx.android.synthetic.main.table_row.view.*
import org.jetbrains.anko.forEachChild

const val NEW_QUIZ_ID : Int = -1

class MngEditQuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_edit_quiz)

        mInitGridQuiz()

        val spnQuizType : Spinner = findViewById(R.id.spnQuizType)
        spnQuizType.setSelection(1)

        val addQuizButton : Button = findViewById<Button>(R.id.btnAddQuiz) as Button
        addQuizButton.setOnClickListener( {
            val selectedId = QuizFactory().getNewQuizId(applicationContext, DB_NAME_MEMORIZER)
            val selectedType = mGetSelectedTypeForAdd()
            mStartActivityEditQuiz(selectedType, selectedId)
        }
        )

        val editQuizButton : Button = findViewById<Button>(R.id.btnEditQuiz) as Button
        editQuizButton.setOnClickListener( {
            val selectedType = mGetSelectedTypeForEdit()
            val selectedId = mGetSelectedQuizId()
            if ((selectedType != null) && (selectedId != null)){
                mStartActivityEditQuiz(selectedType, selectedId)
            }
        }
        )

        val deleteQuizButton : Button = findViewById<Button>(R.id.btnDeleteQuiz) as Button
        deleteQuizButton.setOnClickListener( {
            val selectedId = mGetSelectedQuizId()
            if (selectedId != null) {
                QuizFactory().deleteQuiz(applicationContext, DB_NAME_MEMORIZER, selectedId)
                // 表をリロードする
                mInitGridQuiz()
            }
        }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == EnumRequestCodes.EDIT_HAND_ACTION.rawValue)
            || (requestCode == EnumRequestCodes.EDIT_TEXT.rawValue)){
            // キャンセルされた
            if (resultCode == Activity.RESULT_CANCELED) {
                // 何もしない
            }
            // Saveされた
            else if (resultCode == Activity.RESULT_OK) {
                // 表をリロードする
                mInitGridQuiz()
            }
        }
    }

    private fun mInitGridQuiz()
    {
        val tableLayout = findViewById<TableLayout>(R.id.lyoQuiz) as TableLayout
        val childCount = tableLayout.childCount
        tableLayout.removeViewsInLayout(1, childCount-1)
        val quizList = QuizFactory().loadAllList(applicationContext, DB_NAME_MEMORIZER)

        quizList.forEachWithIndex { quizIndex, q ->
            val tableRow = layoutInflater.inflate(R.layout.table_row, null) as TableRow
            val quiz = QuizFactory().load(applicationContext, DB_NAME_MEMORIZER, q.id)

            val radio = tableRow.findViewById<RadioButton>(R.id.radio1) as RadioButton
            // デフォルトで一行目をチェック
            if (quizIndex == 0) {
                radio.isChecked = true
            }
            // チェックを付けたら他の行をアンチェック
            radio.setOnCheckedChangeListener( {
                rdo , checked ->
                if (checked) {
                    mAlternateRadioButton(rdo)
                }
            })
            val id = tableRow.findViewById<TextView>(R.id.row_text_id) as TextView
            id.text = quiz.id.toString()
            val type = tableRow.findViewById<TextView>(R.id.row_text_type) as TextView
            type.text = quiz.type.toString()
            val title = tableRow.findViewById<TextView>(R.id.row_text_title) as TextView
            title.text = quiz.title
            val updateDate = tableRow.findViewById<TextView>(R.id.row_text_update_date) as TextView
            updateDate.text = quiz.updateDate

            // ツートン表示
            if ((quizIndex + 1) % 2 == 0) {
//                val color = resources.getColor(R.color.chart_dg_gray)
                val color = resources.getColor(R.color.abc_background_cache_hint_selector_material_dark)
                id.setBackgroundColor(color)
                type.setBackgroundColor(color)
                title.setBackgroundColor(color)
                updateDate.setBackgroundColor(color)
            }

            tableLayout.addView(tableRow, TableLayout.LayoutParams())
        }
    }

    private fun mAlternateRadioButton(btn: CompoundButton)
    {
        val tableLayout = findViewById<TableLayout>(R.id.lyoQuiz) as TableLayout
        tableLayout.forEachChild {
            val tableRow = it as TableRow
            if (( tableRow.radio1 != null ) && (tableRow.radio1 != btn)) {
                tableRow.radio1.isChecked = false
            }
        }
    }

    private fun mGetSelectedQuizId() : Int?
    {
        val tableLayout = findViewById<TableLayout>(R.id.lyoQuiz) as TableLayout
        tableLayout.forEachChild {
            val tableRow = it as TableRow
            if (( tableRow.radio1 != null ) && tableRow.radio1.isChecked) {
                return tableRow.row_text_id.text.toString().toInt()
            }
        }
        return null
    }

    private fun mGetSelectedTypeForEdit() : EnumQuizType?
    {
        val tableLayout = findViewById<TableLayout>(R.id.lyoQuiz) as TableLayout
        tableLayout.forEachChild {
            val tableRow = it as TableRow
            if (( tableRow.radio1 != null ) && tableRow.radio1.isChecked) {
                return EnumQuizType.fromString(tableRow.row_text_type.text.toString())
            }
        }
        return null
    }

    private fun mGetSelectedTypeForAdd() : EnumQuizType {
        val sqnQuizType =  findViewById<Spinner>(R.id.spnQuizType)
        return EnumQuizType.fromInt(sqnQuizType.selectedItemPosition)
    }

    private fun mStartActivityEditQuiz(selectedType: EnumQuizType, selectedId : Int) {
        var reqCode : Int
        var intent : Intent

        when (selectedType) {
            EnumQuizType.HAND_ACTION -> {
                reqCode = EnumRequestCodes.EDIT_HAND_ACTION.rawValue
                intent =  Intent(application, MngEditQzHandActionActivity::class.java)
            }
            EnumQuizType.TEXT -> {
                reqCode = EnumRequestCodes.EDIT_TEXT.rawValue
                intent = Intent(application, MngEditQzTextActivity::class.java)
            }
        }
        intent.putExtra(INTENT_KEY_QUIZ_ID, selectedId)
        startActivityForResult(intent, reqCode)
    }
}
