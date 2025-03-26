package com.example.numbuddy.utility

import Question
import kotlin.random.Random

object QuestionGenerator {
    fun generateBasicAdditionQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()

        repeat(count) {
            val num1 = Random.nextInt(1, 11)
            val num2 = Random.nextInt(1, 11)
            val correctAnswer = num1 + num2

            val options = mutableSetOf(correctAnswer)
            while (options.size < 4) {
                options.add(Random.nextInt(2, 21))
            }

            val optionsList = options.toList().shuffled()

            // Find the correct answer’s index (1-based)
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            // Build the question
            questions.add(
                Question(
                    question = "$num1 + $num2 = ?",
                    choice1 = optionsList[0],
                    choice2 = optionsList[1],
                    choice3 = optionsList[2],
                    choice4 = optionsList[3],
                    correct = correctIndex
                )
            )
        }

        return questions
    }
}
