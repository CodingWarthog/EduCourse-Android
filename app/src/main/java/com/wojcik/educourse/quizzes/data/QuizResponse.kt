package com.wojcik.educourse.quizzes.data

data class QuizResponse(val id : Int, val name : String?, val subject : String?, val timeLimit : Int, val totalExamPoints : Int,
                   val numberOfQuestions : Int ,val level : String, val type : String, val courseId: Int, val question : List<QuestionResponse>) {
}