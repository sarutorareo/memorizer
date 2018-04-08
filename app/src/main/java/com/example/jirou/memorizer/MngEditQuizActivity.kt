package com.example.jirou.memorizer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MngEditQuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_edit_quiz)

        val addQzHandActionButton : Button = findViewById(R.id.btnAddQzHandAction)
        addQzHandActionButton.setOnClickListener( {
            val intent = Intent(application, MngAddQzHandActionActivity::class.java)
            startActivity(intent)
        }
        )
    }
}
