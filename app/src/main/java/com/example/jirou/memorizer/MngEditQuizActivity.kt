package com.example.jirou.memorizer

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.db.MemorizeDBSQLDroidHelper

class MngEditQuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_edit_quiz)

        val addQzHandActionButton : Button = findViewById<Button>(R.id.btnEditQzHandAction) as Button
        addQzHandActionButton.setOnClickListener( {
            val intent = Intent(application, MngEditQzHandActionActivity::class.java)
            intent.putExtra(INTENT_KEY_QUIZ_ID, 0)
            startActivityForResult(intent, REQUEST_CODE_EDIT_HAND_ACTION)
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
}
