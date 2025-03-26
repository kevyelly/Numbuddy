package com.example.numbuddy

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.numbuddy.utility.QuestionGenerator

class QuizActivity : ComponentActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var choice1: Button
    private lateinit var choice2: Button
    private lateinit var choice3: Button
    private lateinit var choice4: Button

    private val questions = QuestionGenerator.generateBasicAdditionQuestions(10)
    private var currentQuestionIndex = 0
    private var score = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quizitem)

        questionTextView = findViewById(R.id.question)
        choice1 = findViewById(R.id.choice1)
        choice2 = findViewById(R.id.choice2)
        choice3 = findViewById(R.id.choice3)
        choice4 = findViewById(R.id.choice4)
        loadQuestion()


        val choices = listOf(choice1, choice2, choice3, choice4)
        choices.forEachIndexed { index, button ->
            button.setOnClickListener {
                checkAnswer(index + 1, button)
            }
        }
    }

    private fun loadQuestion() {
        if (currentQuestionIndex < 10) {
            val currentQuestion = questions[currentQuestionIndex]

            // Update UI
            questionTextView.text = currentQuestion.question
            choice1.text = currentQuestion.choice1.toString()
            choice2.text = currentQuestion.choice2.toString()
            choice3.text = currentQuestion.choice3.toString()
            choice4.text = currentQuestion.choice4.toString()

            // Reset button colors and enable them

            val choices = listOf(choice1, choice2, choice3, choice4)
            var ctr = 1;
            choices.forEach {
                if(ctr % 2 != 0){
                    it.setBackgroundColor(Color.parseColor("#7ED957"))
                }else{
                    it.setBackgroundResource(R.drawable.cancelbutton_border)
                }
                it.isEnabled = true
                ctr++;
            }
        } else {
            Toast.makeText(this, "Quiz Finished! Score: $score / ${questions.size}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun checkAnswer(selectedIndex: Int, selectedButton: Button) {
        val currentQuestion = questions[currentQuestionIndex]

        val choices = listOf(choice1, choice2, choice3, choice4)
        choices.forEach { it.isEnabled = false }

        if (selectedIndex == currentQuestion.correct) {
            score++
            selectedButton.setBackgroundColor(Color.GREEN)
        } else {
            selectedButton.setBackgroundColor(Color.RED)
            choices[currentQuestion.correct - 1].setBackgroundColor(Color.GREEN)
        }

        handler.postDelayed({
            currentQuestionIndex++
            loadQuestion()
        }, 1000)
    }
}
