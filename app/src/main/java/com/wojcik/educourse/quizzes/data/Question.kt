package com.wojcik.educourse.quizzes.data

import java.util.*

data class Question(val id: Int, val questionContent: String, var answerA: String, var answerB: String, var answerC: String, var answerD: String, val correctAnswer: String, val quizId: Int) {
    var userAnswer : String = ""

    init {
        val answerList = ArrayList<String>()
        answerList.add(answerA)
        answerList.add(answerB)
        answerList.add(answerC)
        answerList.add(answerD)
        answerList.shuffle()
        answerA = answerList[0]
        answerB = answerList[1]
        answerC = answerList[2]
        answerD = answerList[3]
    }
}