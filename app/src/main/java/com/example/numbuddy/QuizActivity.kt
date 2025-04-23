package com.example.numbuddy

import android.app.Dialog
import android.content.Intent
import android.view.Window
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.example.numbuddy.utility.Question
import com.example.numbuddy.utility.QuestionGenerator
import com.example.numbuddy.utility.SessionManager
import com.example.numbuddy.utility.UserProgressManager

class QuizActivity : ComponentActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var choice1: Button
    private lateinit var choice2: Button
    private lateinit var choice3: Button
    private lateinit var choice4: Button
    private lateinit var indicator: TextView
    private lateinit var backButton: ImageView

    private var currentQuestionIndex = 0
    private var score = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var questions: List<Question>
    private var questionType: Int = 1
    private var autoDismissRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quizitem)

        indicator = findViewById(R.id.indexindicator)
        questionTextView = findViewById(R.id.question)
        choice1 = findViewById(R.id.choice1)
        choice2 = findViewById(R.id.choice2)
        choice3 = findViewById(R.id.choice3)
        choice4 = findViewById(R.id.choice4)
        backButton = findViewById(R.id.bckbutton)

        questionType = intent.getIntExtra("QUESTION_TYPE", 1)
        questions = generateQuestionsForType(questionType)

        loadQuestion()

        val choices = listOf(choice1, choice2, choice3, choice4)
        choices.forEachIndexed { index, button ->
            button.setOnClickListener {
                choices.forEach { btn -> btn.isEnabled = false }
                checkAnswer(index + 1, button)
            }
        }

        backButton.setOnClickListener {
            showExitConfirmationDialog()
        }
    }

    private fun generateQuestionsForType(type: Int): List<Question> {
        val questionList = when (type) {
            2 -> QuestionGenerator.generateBasicAdditionQuestions(10)
            3 -> QuestionGenerator.generateBasicSubtractionQuestions(10)
            4 -> {
                val divisionQuestions = QuestionGenerator.generateBasicDivisionQuestions(5)
                val multiplicationQuestions = QuestionGenerator.generateBasicMultiplicationQuestions(5)
                (divisionQuestions + multiplicationQuestions).shuffled()
            }
            5 -> {
                val decompQuestions = QuestionGenerator.generateBasicNumberDecompositionQuestions(5)
                val builderQuestions = QuestionGenerator.generateNumberBuilderQuestions(5)
                (decompQuestions + builderQuestions).shuffled()
            }
            6 -> {
                val advAddQuestions = QuestionGenerator.generateAdvanceAdditionQuestions(5)
                val advSubQuestions = QuestionGenerator.generateAdvanceSubtractionQuestions(5)
                (advAddQuestions + advSubQuestions).shuffled()
            }
            else -> QuestionGenerator.generateLessOrGreaterQuestions(10)
        }
        return questionList.take(10)
    }

    private fun loadQuestion() {
        Log.d("QuizActivity", "Loading question for index: $currentQuestionIndex")
        if (currentQuestionIndex < questions.size) {
            val currentQuestion = questions[currentQuestionIndex]
            questionTextView.text = currentQuestion.question
            choice1.text = currentQuestion.choice1.toString()
            choice2.text = currentQuestion.choice2.toString()
            choice3.text = currentQuestion.choice3.toString()
            choice4.text = currentQuestion.choice4.toString()
            val displayIndex = currentQuestionIndex + 1
            indicator.text = "Question $displayIndex/${questions.size}"
            resetButtonAppearances()
            val choices = listOf(choice1, choice2, choice3, choice4)
            choices.forEach { it.isEnabled = true }
        } else {
            Log.d("QuizActivity", "Quiz finished, calling handleQuizCompletion.")
            handleQuizCompletion()
        }
    }

    private fun checkAnswer(selectedIndex: Int, selectedButton: Button) {
        Log.d("QuizActivity", "Checking answer for index: $currentQuestionIndex")
        val currentQuestion = questions[currentQuestionIndex]
        val isCorrect = selectedIndex == currentQuestion.correct
        if (isCorrect) {
            score++
            selectedButton.setBackgroundColor(Color.GREEN)
            showFeedbackPopup(true)
        } else {
            selectedButton.setBackgroundColor(Color.RED)
            val correctButtonIndex = currentQuestion.correct - 1
            if (correctButtonIndex in 0..3) {
                listOf(choice1, choice2, choice3, choice4)[correctButtonIndex].setBackgroundColor(Color.GREEN)
            }
            showFeedbackPopup(false)
        }
    }

    private fun handleQuizCompletion() {
        Log.d("QuizActivity", "Handling quiz completion.")
        val user = SessionManager.loggedInUser
        if (user != null) {
            val username = user.username
            var levelIncreased = false

            try {
                val currentCompleted = UserProgressManager.getQuizzesCompleted(this, username)
                UserProgressManager.saveQuizzesCompleted(this, username, currentCompleted + 1)
                Log.d("QuizActivity", "Quizzes completed count updated for $username")
            } catch (e: Exception) {
                Log.e("QuizActivity", "Failed to update quizzes completed count", e)
            }

            try {
                val currentStars = UserProgressManager.getUserStars(this, username)
                UserProgressManager.saveUserStars(this, username, currentStars + 1)
                Log.d("QuizActivity", "Stars count updated for $username")
            } catch (e: Exception) {
                Log.e("QuizActivity", "Failed to update stars count", e)
            }

            if (score >= 8) {
                try {
                    val currentLevelFromPrefs = UserProgressManager.getUserStageLevel(this, username)
                    Log.d("QuizActivity", "Checking level progression: current=$currentLevelFromPrefs, type=$questionType")
                    if (currentLevelFromPrefs == questionType) {
                        val newLevel = currentLevelFromPrefs + 1
                        UserProgressManager.saveUserStageLevel(this, username, newLevel)
                        levelIncreased = true
                        Log.d("QuizActivity", "Level increased to $newLevel for $username")
                    }
                } catch (e: Exception) {
                    Log.e("QuizActivity", "Failed to update stage level", e)
                }
            }
            showQuizResultDialog(score, levelIncreased)
        } else {
            Log.w("QuizActivity", "User not logged in during handleQuizCompletion")
            showQuizResultDialog(score, false)
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun resetButtonAppearances() {
        choice1.setBackgroundColor(Color.parseColor("#7ED957"))
        choice2.setBackgroundResource(R.drawable.cancelbutton_border)
        choice3.setBackgroundColor(Color.parseColor("#7ED957"))
        choice4.setBackgroundResource(R.drawable.cancelbutton_border)
    }

    private fun showFeedbackPopup(isCorrect: Boolean) {
        if (isFinishing || isDestroyed) return
        Log.d("QuizActivity", "Showing feedback popup: isCorrect=$isCorrect")

        val dialogView = layoutInflater.inflate(R.layout.quizpopup, null)
        val animationView = dialogView.findViewById<LottieAnimationView>(R.id.lottie_animation_view)
        val resultTextView = dialogView.findViewById<TextView>(R.id.resulttext)

        animationView.visibility = View.VISIBLE
        resultTextView.visibility = View.VISIBLE

        var hasAdvanced = false
        val advanceQuizAction = {
            autoDismissRunnable?.let { handler.removeCallbacks(it) }
            if (!hasAdvanced && !isFinishing && !isDestroyed) {
                hasAdvanced = true
                currentQuestionIndex++
                Log.d("QuizActivity", "Advancing to next question.")
                loadQuestion()
            }
        }

        if (isCorrect) {
            animationView.setAnimation(R.raw.success)
            resultTextView.text = "CORRECT!"
            resultTextView.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            animationView.setAnimation(R.raw.fail)
            resultTextView.text = "TRY AGAIN"
            resultTextView.setTextColor(Color.parseColor("#F44336"))
        }
        animationView.playAnimation()

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        autoDismissRunnable = Runnable {
            Log.d("QuizActivity", "Auto-dismiss timer fired.")
            if (dialog.isShowing && !isFinishing && !isDestroyed) {
                dialog.dismiss()
                advanceQuizAction()
            }
        }

        dialogView.setOnClickListener {
            Log.d("QuizActivity", "Feedback popup tapped.")
            autoDismissRunnable?.let { handler.removeCallbacks(it) }
            if (dialog.isShowing && !isFinishing && !isDestroyed) {
                dialog.dismiss()
                advanceQuizAction()
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (!isFinishing && !isDestroyed) {
            dialog.show()
            Log.d("QuizActivity", "Scheduling auto-dismiss for feedback popup.")
            handler.postDelayed(autoDismissRunnable!!, 2500)
        } else {
            return
        }

        val widthInDp = 300; val heightInDp = 300
        val scale = Resources.getSystem().displayMetrics.density
        dialog.window?.setLayout((widthInDp * scale).toInt(), (heightInDp * scale).toInt())
    }


    private fun showQuizResultDialog(finalScore: Int, levelIncreased: Boolean) {
        if (isFinishing || isDestroyed) return
        Log.d("QuizActivity", "Showing results dialog: score=$finalScore, levelIncreased=$levelIncreased")

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.quizresult)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val scoreTextView = dialog.findViewById<TextView>(R.id.results_score_textview)
        val messageTextView = dialog.findViewById<TextView>(R.id.results_message_textview)
        val okButton = dialog.findViewById<Button>(R.id.results_ok_button)
        val iconImageView = dialog.findViewById<ImageView>(R.id.results_icon_imageview)

        scoreTextView.text = "Score: $finalScore / ${questions.size}"
        var message = ""
        if (levelIncreased) { message = "Level Up!" }
        else if (finalScore >= 8) { message = "Great Job!" }
        else { message = "Keep Practicing!"; iconImageView.visibility = View.GONE }
        messageTextView.text = message

        okButton.setOnClickListener {
            Log.d("QuizActivity", "Results OK button clicked.")
            if(!isFinishing && !isDestroyed) {
                dialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        if (!isFinishing && !isDestroyed) {
            dialog.show()
        }
    }

    private fun showExitConfirmationDialog() {
        if (isFinishing || isDestroyed) return
        AlertDialog.Builder(this)
            .setTitle("Exit Quiz?")
            .setMessage("Are you sure you want to exit? Your progress in this quiz will be lost.")
            .setPositiveButton("Exit") { _, _ -> if(!isFinishing && !isDestroyed) finish() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showExitConfirmationDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        autoDismissRunnable?.let { handler.removeCallbacks(it) }
        Log.d("QuizActivity", "onDestroy called.")
    }
}