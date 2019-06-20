package com.wojcik.educourse.quizzes.data

data class MyExamResponse(val id : Int, val name : String, val subject : String, val timeLimit : Int,
                     val totalExamPoints : Int, val numberOfQuestions : Int, val level : String,
                     val type : String, val courseId : Int) {
}