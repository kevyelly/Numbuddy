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
    fun generateBasicSubtractionQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()

        repeat(count) {
            val num1 = Random.nextInt(0, 21)
            val num2 = Random.nextInt(0, num1 + 1) // Avoid negative results

            val correctAnswer = num1 - num2

            val options = mutableSetOf(correctAnswer)

            // Add three other choices but must unique and reasonably close to the correct answer
            while (options.size < 4) {
                val offset = Random.nextInt(-5, 6) // Range: -5 to +5
                val wrongAnswer = correctAnswer + offset

                if (wrongAnswer != correctAnswer && wrongAnswer in 0..20) {
                    options.add(wrongAnswer)
                }
            }

            val optionsList = options.toList().shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            questions.add(
                Question(
                    question = "$num1 - $num2 = ?",
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
    fun generateBasicMultiplicationQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()

        repeat(count) {
            val num1 = Random.nextInt(0, 11)
            val num2 = Random.nextInt(0, 11)
            val correctAnswer = num1 * num2

            val options = mutableSetOf(correctAnswer)

            // Generate 3 wrong answers; must be unique and must be reasonably close to the correct ans
            while (options.size < 4) {
                val offset = Random.nextInt(-10, 11) // Offset range: -10 to +10
                val wrongAnswer = correctAnswer + offset

                if (wrongAnswer != correctAnswer && wrongAnswer in 0..100) {
                    options.add(wrongAnswer)
                }
            }

            val optionsList = options.toList().shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            questions.add(
                Question(
                    question = "$num1 × $num2 = ?",
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
    fun generateBasicDivisionQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()

        repeat(count) {
            val correctAnswer = Random.nextInt(1, 11) // 1 to 10
            val divisor = Random.nextInt(1, 11)
            val dividend = correctAnswer * divisor

            // Ensure the dividend stays within 0 to 100
            if (dividend > 100) {
                return@repeat
            }

            val options = mutableSetOf<Int>()
            options.add(correctAnswer)

            // Generate 3 wrong but close answers
            while (options.size < 4) {
                val offset = Random.nextInt(-3, 4)
                val wrongAnswer = correctAnswer + offset

                if (wrongAnswer != correctAnswer && wrongAnswer in 0..12) {
                    options.add(wrongAnswer)
                }
            }

            val optionsList = options.toList().shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            questions.add(
                Question(
                    question = "$dividend ÷ $divisor = ?",
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

    fun generateLessOrGreaterProblem(count: Int): List<Question> {
        val questions = mutableListOf<Question>()

        repeat(count) {
            val isLessThan = Random.nextBoolean()
            val limit = Random.nextInt(5, 36) // Avoid edge cases for sequencing (needs space on both sides)
            val conditionText = if (isLessThan) "__ < $limit" else "__ > $limit"

            val correctAnswer = if (isLessThan) {
                (limit - Random.nextInt(1, 4)).coerceAtLeast(0)
            } else {
                (limit + Random.nextInt(1, 4)).coerceAtMost(40)
            }

            val options = mutableSetOf(correctAnswer)

            // Generate 3 sequential, close, and incorrect values
            var offset = 1
            while (options.size < 4) {
                val wrongAnswer = if (isLessThan) {
                    (limit + offset).coerceAtMost(40)
                } else {
                    (limit - offset).coerceAtLeast(0)
                }

                if (wrongAnswer != correctAnswer) {
                    options.add(wrongAnswer)
                }

                offset++
            }

            val optionsList = options.toList().shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            questions.add(
                Question(
                    question = conditionText,
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
