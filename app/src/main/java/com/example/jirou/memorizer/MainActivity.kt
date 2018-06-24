package com.example.jirou.memorizer

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.db.MemorizeDBSQLDroidHelper
import com.example.jirou.memorizer.models.*

class MainActivity : AppCompatActivity() {
    private var mTrainingManager : TrainingManager = TrainingManager(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val startButton : Button = findViewById(R.id.btnStart)
        startButton.setOnClickListener( {
            val quizIdStr = (findViewById<TextView>(R.id.txtQuizId)).text.toString()
            var quizList : List<Quiz> =
            when {
                (quizIdStr.isBlank()) -> mGetAllQuizList()
                else -> mGetQuizList(quizIdStr.toInt())
            }
            mTrainingManager = TrainingManager(quizList)
            val quiz = mTrainingManager.start()

            val intent = Intent(application, quiz.activity)
            intent.putExtra(INTENT_KEY_QUIZ_ID, quiz.id)
            startActivityForResult(intent, quiz.requestCode.rawValue)
        }
        )

        val editButton : Button = findViewById(R.id.btnManageQuiz)
        editButton.setOnClickListener( {
            val intent = Intent(application, MngEditQuizActivity::class.java)
            startActivity(intent)
        }
        )

        val initDBSchemaButton : Button = findViewById(R.id.btnInitDBSchema)
        initDBSchemaButton.setOnClickListener( {
            MemorizeDBSQLDroidHelper.initDBSchema(applicationContext, DB_NAME_MEMORIZER)
        }
        )

        /*
        val dropDBSchemaButton : Button = findViewById(R.id.btnDropDBSchema)
        dropDBSchemaButton.setOnClickListener( {
            MemorizeDBSQLDroidHelper.dropDBSchema(applicationContext, DB_NAME_MEMORIZER)
        }
        )
        */
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun mGetAllQuizList(): List<Quiz>
    {
        return  QuizFactory().loadAllList(applicationContext, DB_NAME_MEMORIZER)
    }

    private fun mGetQuizList(id : Int): List<Quiz>{
        return arrayListOf(QuizFactory().load(applicationContext, DB_NAME_MEMORIZER, id))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) {
            // 何もしない
        }
        else if (resultCode == Activity.RESULT_OK) {
            // 次のQuizIndexを受け取る
            val receivedIntent = data!!
            val isNext = receivedIntent.getBooleanExtra(INTENT_KEY_NEXT_OR_RETRY, true)
            val nextQuiz =
                    if (isNext) {
                        mTrainingManager.next()
                    } else {
                        mTrainingManager.retry()
                    }
            nextQuiz ?: return
            val sendIntent = Intent(application, nextQuiz.activity)
            sendIntent.putExtra(INTENT_KEY_QUIZ_ID, nextQuiz.id)

            when (requestCode) {
                EnumRequestCodes.TRAINING_HAND_ACTION.rawValue ->
                {
                    mPutExtraHandAction(isNext, receivedIntent, sendIntent)
                }
                EnumRequestCodes.TRAINING_TEXT.rawValue -> {
                    mPutExtraText(isNext, receivedIntent, sendIntent)
                }
            }

            startActivityForResult(sendIntent, requestCode)
        }
    }

    private fun mPutExtraText(isNext: Boolean, receivedIntent: Intent, sendIntent: Intent) {
        Log.e("mPutExtraText", "start")
        if (!isNext) {
            (0..4).forEach {
                Log.e("mPutExtraText", "it = " + it.toString())
                val key = String.format(INTENT_KEY_ANS_FMT, it)
                val ans = receivedIntent.getStringExtra(key)
                Log.e("mPutExtraText", String.format("key = %s, ans = %s", key, ans))
                sendIntent.putExtra(key, ans)
            }
        }
        Log.e("mPutExtraText", "end")
    }

    private fun mPutExtraHandAction(isNext: Boolean, received: Intent, intent: Intent) {
        val handActionList = HandActionList()
        if (!isNext) {
            handActionList.getExtra(received)
        }
        handActionList.putExtra(intent)
    }
}
