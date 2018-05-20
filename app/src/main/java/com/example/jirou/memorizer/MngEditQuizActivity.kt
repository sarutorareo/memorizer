package com.example.jirou.memorizer

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.db.MemorizeDBSQLDroidHelper
import com.example.jirou.memorizer.models.QuizFactory
import org.jetbrains.anko.collections.forEachWithIndex
import kotlinx.android.synthetic.main.table_row.view.*
import org.jetbrains.anko.forEachChild


class MngEditQuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_edit_quiz)

        mInitGridQuiz()

        val addQzHandActionButton : Button = findViewById<Button>(R.id.btnEditQzHandAction) as Button
        addQzHandActionButton.setOnClickListener( {
            val selectedId = mGetSelectedQuizId()
            if (selectedId != null) {
                val intent = Intent(application, MngEditQzHandActionActivity::class.java)
                intent.putExtra(INTENT_KEY_QUIZ_ID, selectedId)
                startActivityForResult(intent, REQUEST_CODE_EDIT_HAND_ACTION)
            }
        }
        )

        val initDBSchemaButton : Button = findViewById(R.id.btnInitDBSchema)
        initDBSchemaButton.setOnClickListener( {
            MemorizeDBSQLDroidHelper.initDBSchema(applicationContext, DB_NAME_MEMORIZER)
        }
        )

        val dropDBSchemaButton : Button = findViewById(R.id.btnDropDBSchema)
        dropDBSchemaButton.setOnClickListener( {
            MemorizeDBSQLDroidHelper.dropDBSchema(applicationContext, DB_NAME_MEMORIZER)
        }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_EDIT_HAND_ACTION && resultCode == Activity.RESULT_OK) {
            // リクエストコードが一致してかつアクティビティが正常に終了していた場合、受け取った値を表示
            val received = data!!
            val receivedId = received.getIntExtra(INTENT_KEY_QUIZ_ID, -1)
            val textView : TextView =  findViewById<TextView>(R.id.txtResultEdit) as TextView
            textView.text = receivedId.toString()

            val intent = Intent(application, MngEditQzHandActionActivity::class.java)
            intent.putExtra(INTENT_KEY_QUIZ_ID, receivedId)
            startActivityForResult(intent, REQUEST_CODE_EDIT_HAND_ACTION)
        }
    }

    private fun mInitGridQuiz()
    {
        val tableLayout = findViewById<TableLayout>(R.id.lyoQuiz) as TableLayout
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
}
