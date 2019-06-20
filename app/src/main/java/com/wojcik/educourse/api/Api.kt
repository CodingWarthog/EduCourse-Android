package com.wojcik.educourse.api

import  com.wojcik.educourse.login.models.LoginResponse
import com.wojcik.educourse.login.models.LoginSend
import com.wojcik.educourse.login.models.RegisterSend
import com.wojcik.educourse.profile.badges.data.BadgeAssignmentResponse
import com.wojcik.educourse.profile.data.UserResponse
import com.wojcik.educourse.quizzes.data.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("auth/login")
    fun login(@Body loginSend: LoginSend) : Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body registerSend: RegisterSend) : Call<Unit>

    @GET("users/information/{username}")
    fun getUserByUsername(@Path("username") username : String) : Call<UserResponse>

    @GET("badges/user_badges/{id}")
    fun getBadgesById(@Path("id") id : Int) : Call<BadgeAssignmentResponse>

    @PUT("users/{id}")
    fun updateUser(@Path("id") id : Int, @Body user : UserResponse ) : Call<Unit>

    @GET("users/courses/{id}")
    fun getMyCourses(@Path("id") id : Int) : Call<List<CourseResponse>>

    @GET("courses/recommendCourses/{id}")
    fun getAvailableCourses(@Path("id") id : Int) : Call<List<CourseResponse>>

    @POST("courseenrolments/")
    fun joinCourse(@Body joinCourseSend: JoinCourseSend) : Call<Unit>

    @GET("exams/courseExams/abcd/{id}")
    fun getExamsById(@Path("id") id : Int) : Call<List<MyExamResponse>>

    @GET("exams/questions/{id}")
    fun getQuestions(@Path("id") id : Int) : Call<ExamQuestionResponse>

    @DELETE("courseenrolments/for_delete")
    fun quitCourse(@Query("user_id") id : Int, @Query("course_id") course : Int) : Call<Unit>

    @PUT("experiences/update_experience/{id}")
    fun updateExperience(@Body updateExperienceSend: UpdateExperienceSend, @Path("id") courseId : Int) : Call<Unit>
}