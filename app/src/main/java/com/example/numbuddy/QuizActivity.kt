package com.example.numbuddy

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import com.example.numbuddy.utility.Question
import com.example.numbuddy.utility.QuestionGenerator
import com.example.numbuddy.utility.SessionManager
import com.example.numbuddy.utility.UserManager

class QuizActivity : ComponentActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var choice1: Button
    private lateinit var choice2: Button
    private lateinit var choice3: Button
    private lateinit var choice4: Button

    private var currentQuestionIndex = 0
    private var score = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var indicator: TextView
    private lateinit var questions: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quizitem)

        indicator = findViewById<TextView>(R.id.indexindicator)

        questionTextView = findViewById(R.id.question)
        choice1 = findViewById(R.id.choice1)
        choice2 = findViewById(R.id.choice2)
        choice3 = findViewById(R.id.choice3)
        choice4 = findViewById(R.id.choice4)

        // Determine which question set to use based on the button pressed in HomePageFragment
        val questionType = intent.getIntExtra("QUESTION_TYPE", 1)

        // Generate a questions based on the button pressed
        questions = when (questionType) {
            2 -> QuestionGenerator.generateBasicAdditionQuestions(10)
            3 -> QuestionGenerator.generateBasicSubtractionQuestions(10)
            4 -> {
                val divisionQuestions = QuestionGenerator.generateBasicDivisionQuestions(5)
                val multiplicationQuestions = QuestionGenerator.generateBasicMultiplicationQuestions(5)
                val combinedQuestions = divisionQuestions + multiplicationQuestions
                combinedQuestions.shuffled()
            }
            5 -> {
                val divisionQuestions = QuestionGenerator.generateBasicNumberDecompositionQuestions(5)
                val multiplicationQuestions = QuestionGenerator.generateNumberBuilderQuestions(5)
                val combinedQuestions = divisionQuestions + multiplicationQuestions
                combinedQuestions.shuffled()
            }
            6 ->{
                val additionprobs = QuestionGenerator.generateAdvanceAdditionQuestions(5)
                val subtractionprobs = QuestionGenerator.generateAdvanceSubtractionQuestions(5)
                val combinedQuestions = additionprobs + subtractionprobs
                combinedQuestions.shuffled()
            }
            else -> QuestionGenerator.generateLessOrGreaterQuestions(10)
        }


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

            questionTextView.text = currentQuestion.question
            choice1.text = currentQuestion.choice1.toString()
            choice2.text = currentQuestion.choice2.toString()
            choice3.text = currentQuestion.choice3.toString()
            choice4.text = currentQuestion.choice4.toString()
            val adjustedIndex = currentQuestionIndex + 1
            val s = "Question $adjustedIndex/10"
            indicator.text = s

            val choices = listOf(choice1, choice2, choice3, choice4)
            var ctr = 1
            choices.forEach {
                if (ctr % 2 != 0) {
                    it.setBackgroundColor(Color.parseColor("#7ED957"))
                } else {
                    it.setBackgroundResource(R.drawable.cancelbutton_border)
                }
                it.isEnabled = true
                ctr++
            }
        } else {
            // if score is 8 or higher, update user level
            val user = SessionManager.loggedInUser
            if (score >= 8 && user != null) {
                val currentUserLevel = user.stageLevel
                val questionType = intent.getIntExtra("QUESTION_TYPE", 1)

                if (currentUserLevel == questionType) {
                    UserManager.incrementUserStageLevel(user.username)
                }
            }

            showQuizResultDialog(score)
            handler.postDelayed({
                Toast.makeText(this, "Quiz Finished! Score: $score / ${questions.size}", Toast.LENGTH_LONG).show()
                finish()
            }, 5000)
        }
    }

    private fun checkAnswer(selectedIndex: Int, selectedButton: Button) {
        val currentQuestion = questions[currentQuestionIndex]

        val choices = listOf(choice1, choice2, choice3, choice4)
        choices.forEach { it.isEnabled = false }

        if (selectedIndex == currentQuestion.correct) {
            score++
            selectedButton.setBackgroundColor(Color.GREEN)
            showQuizResultDialog(-1)
        } else {
            selectedButton.setBackgroundColor(Color.RED)
            choices[currentQuestion.correct - 1].setBackgroundColor(Color.GREEN)
            showQuizResultDialog(-2)
        }

        handler.postDelayed({
            currentQuestionIndex++
            loadQuestion()
        }, 1000)
    }

    private fun showQuizResultDialog(t: Int) {
        val dialogView = layoutInflater.inflate(R.layout.quizpopup, null)
        if (t == -1) dialogView.setBackgroundResource(R.drawable.correctbg)
        else if (t == -2) dialogView.setBackgroundResource(R.drawable.wrongbg)
        else {
            dialogView.setBackgroundColor(Color.WHITE)
            val text = dialogView.findViewById<TextView>(R.id.resulttext)
            val message = "Well done! You achieved $t/10"
            text.text = message
            text.bringToFront()
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val widthInDp = 300
        val heightInDp = 300

        val scale = Resources.getSystem().displayMetrics.density
        val widthInPx = (widthInDp * scale).toInt()
        val heightInPx = (heightInDp * scale).toInt()

        var delay: Long = 0
        if (t == -1 || t == -2) {
            delay = 1500
        } else {
            delay = 5000
        }

        dialog.window?.setLayout(widthInPx, heightInPx)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) dialog.dismiss()
        }, delay)
    }
}
