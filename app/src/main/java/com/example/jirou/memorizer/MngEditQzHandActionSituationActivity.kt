package com.example.jirou.memorizer

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.jirou.memorizer.models.EnumHASituation
import com.example.jirou.memorizer.models.EnumHAPosition
import com.example.jirou.memorizer.models.QuestionHandAction

class MngEditQzHandActionSituationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_edit_qz_hand_action_situation)

        val qstHa  = intent.getParcelableExtra<QuestionHandAction>(INTENT_KEY_QUESTION_HAND_ACTION)

        findViewById<TextView>(R.id.txtSituations).text = qstHa.toString()

        // QuestionHandAction ⇒ ドロップダウンの選択
        mQuestionHandActionToSpinners(qstHa)

        val spinner =  findViewById<Spinner>(R.id.spnSituation)

        Log.e("MngEditQzHandActionSitu",
                String.format("selectedItem = %s, selectedItemId = %d, selectedItemPosition = %d",
                        spinner.selectedItem,
                        spinner.selectedItemId,
                        spinner.selectedItemPosition
                        ))

        val okButton : Button = findViewById(R.id.btnOK)
        okButton.setOnClickListener( {
            // ドロップダウンの選択 ⇒ QuestionHandAction
            val resultQstHa = mSpinnersToQuestionHandAction()

            // 戻り値を設定
            val intent = Intent()
            intent.putExtra(INTENT_KEY_QUESTION_HAND_ACTION, resultQstHa)

            // 戻り値を渡して 呼び出し元 の onActivityResult を呼び出す
            setResult(Activity.RESULT_OK, intent)

            // アクティビティを閉じる
            finish()
        }
        )

        val cancelButton : Button = findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener( {
            // アクティビティを閉じる
            finish()
        }
        )
    }

    private fun mSpinnersToQuestionHandAction() : QuestionHandAction
    {
        val sqnSitu =  findViewById<Spinner>(R.id.spnSituation)
        val sqnHeroPos =  findViewById<Spinner>(R.id.spnHeroPosition)
        val sqnOppoPos =  findViewById<Spinner>(R.id.spnOpponentPosition)

        val situ = EnumHASituation.fromInt(sqnSitu.selectedItemPosition)
        val heroPos = EnumHAPosition.fromInt(sqnHeroPos.selectedItemPosition)
        val oppoPos = EnumHAPosition.fromInt(sqnOppoPos.selectedItemPosition)

        return QuestionHandAction(-1, situ, heroPos, oppoPos)
    }

    private fun mQuestionHandActionToSpinners(qstHa : QuestionHandAction)
    {
        val sqnSitu =  findViewById<Spinner>(R.id.spnSituation)
        val sqnHeroPos =  findViewById<Spinner>(R.id.spnHeroPosition)
        val sqnOppoPos =  findViewById<Spinner>(R.id.spnOpponentPosition)

        sqnSitu.setSelection(qstHa.situation.rawValue)
        sqnHeroPos.setSelection(qstHa.heroPosition.rawValue)
        sqnOppoPos.setSelection(qstHa.opponentPosition.rawValue)
    }
}
