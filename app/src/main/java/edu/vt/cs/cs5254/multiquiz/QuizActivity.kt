package edu.vt.cs.cs5254.multiquiz

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import edu.vt.cs.cs5254.multiquiz.Answer
import edu.vt.cs.cs5254.multiquiz.R
import kotlin.collections.forEach as forEach1

const val HINT = "hint count"
const val CORRECT = "correct count"
const val TOTAL = "total count"

class QuizActivity : AppCompatActivity() {

    private val defaultButtonColor = "#00a2ff"
    private val selectedButtonColor = "#cb297b"

    private lateinit var questionTextView: TextView
    private lateinit var answerButtonList: List<Button>
    private lateinit var disableButton: Button
    private lateinit var resetButton: Button

    private val questionList = listOf(
        Question(
            R.string.australia_question,
            listOf(
                Answer(R.string.australia_answer_canberra, true),
                Answer(R.string.australia_answer_brisbane, false),
                Answer(R.string.australia_answer_perth, false),
                Answer(R.string.australia_answer_sidney, false)
            )
        ),
        Question(
            R.string.taiwan_question,
            listOf(
                Answer(R.string.taiwan_answer_hsinchu, false),
                Answer(R.string.taiwan_answer_taipei, true),
                Answer(R.string.taiwan_answer_kaohsiung, false),
                Answer(R.string.taiwan_answer_taichung, false)
            )
        ),
        Question(
            R.string.japan_question,
            listOf(
                Answer(R.string.japan_answer_hokkaido, false),
                Answer(R.string.japan_answer_nara, false),
                Answer(R.string.japan_answer_kyoto, true),
                Answer(R.string.japan_answer_osaka, false)
            )
        ),
        Question(
            R.string.germany_question,
            listOf(
                Answer(R.string.germany_answer_hamburg, false),
                Answer(R.string.germany_answer_heidelberg, false),
                Answer(R.string.germany_answer_hessen, false),
                Answer(R.string.germany_answer_berlin, true)
            )
        )
    )

    var currentQuestionIndex = 0
    var question = questionList[currentQuestionIndex]
    var answerList = question.answers
    var hint_count = 0
    var correct_count = 0
    var total_count = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize views
        questionTextView = findViewById(R.id.question_text_view)
        answerButtonList = listOf(
            findViewById(R.id.answer_0_button),
            findViewById(R.id.answer_1_button),
            findViewById(R.id.answer_2_button),
            findViewById(R.id.answer_3_button)
        )
        disableButton = findViewById(R.id.hint_button)
        resetButton = findViewById(R.id.submit_button)

        for (index: Int in answerButtonList.indices){
            answerButtonList[index].setOnClickListener {
                    processAnswerButtonClick(index)
            }
        }

        disableButton.setOnClickListener {
            processDisableButtonClick()
        }
        resetButton.setOnClickListener {
            processResetButtonClick()
        }

        refreshView()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun processAnswerButtonClick(answerIndex: Int) {
        // deselect only non-clicked answers
        val answer : Answer = answerList[answerIndex]
        answerList.minus(answer).map{it.isSelected = false}
        answer.isSelected = !answer.isSelected
        refreshView()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun processDisableButtonClick() {

       val incorrectAnswer : Answer = answerList.first { it.isEnabled &&!it.isCorrect }
        incorrectAnswer.isEnabled = false
        incorrectAnswer.isSelected = false
        hint_count += 1
        refreshView()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun processResetButtonClick() {
        val selectedAnswer: Answer = answerList.first { it.isSelected }

        if(selectedAnswer.isCorrect){
            Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show()
            correct_count += 1
        }else{
            Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
        }
        for(it: Answer in answerList){
            it.isEnabled = true
            it.isSelected = false
        }

        if (currentQuestionIndex == 3){
            val intent = Intent(this, Result::class.java).apply {
                putExtra(HINT, hint_count.toString())
                putExtra(CORRECT, correct_count.toString())
                putExtra(TOTAL, "4")
            }
            startActivity(intent)
        }

        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size
        question = questionList[currentQuestionIndex]
        answerList = question.answers
        total_count += 1
        refreshView()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun refreshView() {
        questionTextView.setText(question.textResId)
        for ((answer: Answer, button: Button) in answerList.zip(answerButtonList)){
            button.setText(answer.textResId)
        }
        disableButton.setText(R.string.disable_button_text)
        resetButton.setText(R.string.reset_button_text)

        // TODO use forEach with (answer, button) pair and zipped lists
        for ((answer,button) in answerList.zip(answerButtonList)) {
            button.isEnabled = answer.isEnabled
            button.isSelected = answer.isSelected
            if (answer.isSelected) {
                setButtonColor(button, selectedButtonColor)
            } else {
                setButtonColor(button, defaultButtonColor)
            }
            if (!answer.isEnabled) {
                button.alpha = .5f
                // TODO put on its own line after the main loop (use any)

            }
        }

        disableButton.isEnabled = answerList.any{it.isEnabled && !it.isCorrect}
        resetButton.isEnabled = answerList.any { it.isSelected }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setButtonColor(button: Button, colorString: String) {
        button.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(colorString))
        button.setTextColor(Color.WHITE)
        button.alpha = 1f
    }
}
