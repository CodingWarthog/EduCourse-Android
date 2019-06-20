package com.wojcik.educourse.quizzes.data

data class QuestionResponse(val id : Int, val question1 : String, val answer : String, val optionOne : String, val optionTwo : String,
                       val optionThree : String, val optionFour: String, val examId: Int) {
}