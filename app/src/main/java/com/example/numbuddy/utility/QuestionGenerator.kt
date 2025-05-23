package com.example.numbuddy.utility

import kotlin.random.Random

object QuestionGenerator {

    fun generateBasicAdditionQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val used = mutableSetOf<Pair<Int, Int>>()

        while (questions.size < count) {
            val num1 = Random.nextInt(1, 11)
            val num2 = Random.nextInt(1, 11)
            val key = if (num1 < num2) Pair(num1, num2) else Pair(num2, num1)

            if (used.contains(key)) continue
            used.add(key)

            val correctAnswer = num1 + num2
            val options = mutableSetOf(correctAnswer)
            while (options.size < 4) {
                options.add(Random.nextInt(2, 21))
            }

            val optionsList = options.shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            questions.add(
                Question("$num1 + $num2 = ?", optionsList[0], optionsList[1], optionsList[2], optionsList[3], correctIndex)
            )
        }
        return questions
    }

    fun generateBasicSubtractionQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val used = mutableSetOf<Pair<Int, Int>>()

        while (questions.size < count) {
            val num1 = Random.nextInt(0, 21)
            val num2 = Random.nextInt(0, num1 + 1)
            val key = Pair(num1, num2)

            if (used.contains(key)) continue
            used.add(key)

            val correctAnswer = num1 - num2
            val options = mutableSetOf(correctAnswer)

            while (options.size < 4) {
                val offset = Random.nextInt(-5, 6)
                val wrong = correctAnswer + offset
                if (wrong != correctAnswer && wrong in 0..20) options.add(wrong)
            }

            val optionsList = options.shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            questions.add(
                Question("$num1 - $num2 = ?", optionsList[0], optionsList[1], optionsList[2], optionsList[3], correctIndex)
            )
        }

        return questions
    }

    fun generateBasicMultiplicationQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val used = mutableSetOf<Pair<Int, Int>>()

        while (questions.size < count) {
            val num1 = Random.nextInt(0, 11)
            val num2 = Random.nextInt(0, 11)
            val key = if (num1 < num2) Pair(num1, num2) else Pair(num2, num1)

            if (used.contains(key)) continue
            used.add(key)

            val correctAnswer = num1 * num2
            val options = mutableSetOf(correctAnswer)

            while (options.size < 4) {
                val wrong = correctAnswer + Random.nextInt(-10, 11)
                if (wrong != correctAnswer && wrong in 0..100) options.add(wrong)
            }

            val optionsList = options.shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            questions.add(
                Question("$num1 × $num2 = ?", optionsList[0], optionsList[1], optionsList[2], optionsList[3], correctIndex)
            )
        }

        return questions
    }

    fun generateBasicDivisionQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val used = mutableSetOf<Pair<Int, Int>>() // store as (dividend, divisor)

        while (questions.size < count) {
            val correctAnswer = Random.nextInt(1, 11)
            val divisor = Random.nextInt(1, 11)
            val dividend = correctAnswer * divisor

            val key = Pair(dividend, divisor)
            if (dividend > 100 || used.contains(key)) continue
            used.add(key)

            val options = mutableSetOf(correctAnswer)

            while (options.size < 4) {
                val wrong = correctAnswer + Random.nextInt(-3, 4)
                if (wrong != correctAnswer && wrong in 0..12) options.add(wrong)
            }

            val optionsList = options.shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            questions.add(
                Question("$dividend ÷ $divisor = ?", optionsList[0], optionsList[1], optionsList[2], optionsList[3], correctIndex)
            )
        }

        return questions
    }

    fun generateLessOrGreaterQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val used = mutableSetOf<Pair<Boolean, Int>>() //

        while (questions.size < count) {
            val isLessThan = Random.nextBoolean()
            val limit = Random.nextInt(5, 36)
            val key = Pair(isLessThan, limit)

            if (used.contains(key)) continue
            used.add(key)

            val conditionText = if (isLessThan) "__ < $limit" else "__ > $limit"
            val correctAnswer = if (isLessThan) {
                (limit - Random.nextInt(1, 4)).coerceAtLeast(0)
            } else {
                (limit + Random.nextInt(1, 4)).coerceAtMost(40)
            }

            val options = mutableSetOf(correctAnswer)
            var offset = 1
            while (options.size < 4) {
                val wrong = if (isLessThan) {
                    (limit + offset).coerceAtMost(40)
                } else {
                    (limit - offset).coerceAtLeast(0)
                }
                if (wrong != correctAnswer) options.add(wrong)
                offset++
            }

            val optionsList = options.shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            questions.add(
                Question(conditionText, optionsList[0], optionsList[1], optionsList[2], optionsList[3], correctIndex)
            )
        }

        return questions
    }
    fun generateBasicNumberDecompositionQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()

        repeat(count) {
            val number = Random.nextInt(10, 1000)

            val ones = number % 10
            val tens = (number / 10) % 10
            val hundreds = (number / 100)

            val decompositionType = Random.nextInt(1, 4)
            val correctAnswer: Int
            val questionText: String

            when (decompositionType) {
                1 -> {
                    correctAnswer = ones
                    questionText = "Value of ones in $number"
                }
                2 -> {
                    correctAnswer = tens
                    questionText = "Value of tens in $number"
                }
                else -> {
                    correctAnswer = hundreds
                    questionText = "Value of hundreds in $number"
                }
            }

            val choices = hashSetOf(correctAnswer)
            while (choices.size < 4) {
                val wrong = Random.nextInt(0, 10)
                if (wrong != correctAnswer) choices.add(wrong)
            }

            val optionsList = choices.toList().shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            questions.add(
                Question(
                    question = questionText,
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

    fun generateNumberBuilderQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val usedCorrectAnswers = mutableSetOf<Int>()

        while (questions.size < count) {

            val hundreds = Random.nextInt(1, 10)
            val tens = Random.nextInt(0, 10)
            val ones = Random.nextInt(0, 10)
            val correctAnswer = hundreds * 100 + tens * 10 + ones

            if (usedCorrectAnswers.contains(correctAnswer)) continue
            usedCorrectAnswers.add(correctAnswer)

            // Create the question text
            val labels = arrayOf(
                "$hundreds hundreds",
                "$tens tens",
                "$ones ones"
            )
            labels.shuffle()
            val questionText = labels.joinToString("\n")

            val options = hashSetOf(correctAnswer)

            while (options.size < 4) {

                val wrongAnswer = Random.nextInt(100, 1000)

                if (wrongAnswer != correctAnswer) {
                    options.add(wrongAnswer)
                }
            }


            val optionsList = options.toList().shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

            questions.add(
                Question(
                    question = questionText,
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
    fun generateAdvanceAdditionQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val used = mutableSetOf<Pair<Int, Int>>()

        while (questions.size < count) {
            val num1 = Random.nextInt(50, 800)
            val num2 = Random.nextInt(50, 800)
            val correctAnswer = num1 + num2

            if (correctAnswer !in 100..999) continue

            val key = if (num1 < num2) Pair(num1, num2) else Pair(num2, num1)
            if (used.contains(key)) continue
            used.add(key)

            val options = mutableSetOf(correctAnswer)

            // Generate incorrrect answers
            while (options.size < 4) {
                val offset = Random.nextInt(-15, 16)
                val wrongAnswer = correctAnswer + offset
                if (wrongAnswer != correctAnswer && wrongAnswer in 100..999) {
                    options.add(wrongAnswer)
                }
            }

            val optionsList = options.shuffled()
            val correctIndex = optionsList.indexOf(correctAnswer) + 1

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
    fun generateAdvanceSubtractionQuestions(count: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val used = mutableSetOf<Pair<Int, Int>>()

        while (questions.size < count) {
            val num1 = Random.nextInt(200, 1000)
            val num2 = Random.nextInt(50, num1)
            val correctAnswer = num1 - num2

            if (correctAnswer !in 100..999) continue

            val key = Pair(num1, num2)
            if (used.contains(key)) continue
            used.add(key)

            val options = mutableSetOf(correctAnswer)
            while (options.size < 4) {
                val offset = Random.nextInt(-15, 16)
                val wrong = correctAnswer + offset
                if (wrong != correctAnswer && wrong in 100..999) {
                    options.add(wrong)
                }
            }

            val optionsList = options.shuffled()
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

}
