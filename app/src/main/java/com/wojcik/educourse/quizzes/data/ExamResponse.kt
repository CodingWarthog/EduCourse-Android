package com.wojcik.educourse.quizzes.data

data class ExamResponse(val id : Int, val categoryId : Int, val myExams : List<MyExamResponse>) {
}