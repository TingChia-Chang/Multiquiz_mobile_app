package edu.vt.cs.cs5254.multiquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Result : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val question_result = intent.getStringExtra(TOTAL)
        val correct_result = intent.getStringExtra(CORRECT)
        val hint_result = intent.getStringExtra(HINT)
        val textView_question = findViewById<TextView>(R.id.total_questions_value).apply {
            text = question_result
        }
        val textView_correct = findViewById<TextView>(R.id.total_correct_value).apply {
            text = correct_result
        }
        val textView_hint = findViewById<TextView>(R.id.hints_used_value).apply {
            text = hint_result
        }

    }
}
