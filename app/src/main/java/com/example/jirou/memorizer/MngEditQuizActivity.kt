package com.example.jirou.memorizer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.jirou.memorizer.db.DB_NAME_MEMORIZER
import com.example.jirou.memorizer.db.MemorizeDBSQLDroidHelper

class MngEditQuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_edit_quiz)

        val addQzHandActionButton : Button = findViewById(R.id.btnEditQzHandAction)
        addQzHandActionButton.setOnClickListener( {
            val intent = Intent(application, MngEditQzHandActionActivity::class.java)
            startActivity(intent)
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
}
