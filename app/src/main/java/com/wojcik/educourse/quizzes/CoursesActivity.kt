package com.wojcik.educourse.quizzes

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.wojcik.educourse.R
import com.wojcik.educourse.api.RetrofitClient
import com.wojcik.educourse.quizzes.data.CourseResponse
import com.wojcik.educourse.quizzes.data.JoinCourseSend
import kotlinx.android.synthetic.main.activity_courses.*
import kotlinx.android.synthetic.main.activity_pofile.*
import kotlinx.android.synthetic.main.item_course.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CoursesActivity : AppCompatActivity() {

    private var userId = 0
    private var myCoursesSelected = true
    private var myCoursesList : ArrayList<CourseResponse> = ArrayList()
    private var availableCoursesList : ArrayList<CourseResponse> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val b : Bundle = intent.extras!!
        userId = b.getInt("userId")
        onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun myCoursesClick(view : View) {
        if(!myCoursesSelected) {
            myCoursesSelected = true
            my_courses_line.setBackgroundResource(R.color.selectedButtonTextColor)
            available_courses_line.setBackgroundResource(R.color.selectedBtn)
            loadMyCourses()
        }
    }

    override fun onResume() {
        super.onResume()
        myCoursesList.clear()
        availableCoursesList.clear()
        if(userId != 0) {
            loadData()
        }
    }

    fun availableCoursesClick(view : View) {
        if(myCoursesSelected) {
            myCoursesSelected = false
            my_courses_line.setBackgroundResource(R.color.selectedBtn)
            available_courses_line.setBackgroundResource(R.color.selectedButtonTextColor)
            loadAvailableCourses()
        }
    }

    fun getMyCoursesSelected(): Boolean {
        return myCoursesSelected
    }

    fun goToExams(position: Int) {
        val intent = Intent(applicationContext, QuizzesActivity::class.java)
        val b = Bundle()
        b.putInt("userId", userId)
        b.putInt("courseId", myCoursesList[position].id)
        b.putString("courseName", myCoursesList[position].name)
        b.putInt("categoryId", myCoursesList[position].categoryId)
        intent.putExtras(b)
        startActivity(intent)
    }

    private fun loadMyCourses() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewCourses.layoutManager = layoutManager
        recyclerViewCourses.removeAllViews()

        val adapter = CoursesAdapter(this, myCoursesList)
        recyclerViewCourses.adapter = adapter
    }


    private fun loadAvailableCourses() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewCourses.layoutManager = layoutManager
        recyclerViewCourses.removeAllViews()

        val adapter = CoursesAdapter(this, availableCoursesList)
        recyclerViewCourses.adapter = adapter
    }

    fun joinCourse(courseId : Int) {
        val completeDialog = AlertDialog.Builder(this)
        completeDialog.setMessage("Czy chcesz zapisać się na kurs?")
        completeDialog.setCancelable(true)
        completeDialog.setPositiveButton(R.string.dialog_positive) { _: DialogInterface, _: Int ->
            val date = "12/12/2009"
            RetrofitClient.instance.joinCourse(JoinCourseSend(courseId, date, userId ))
                .enqueue(object: Callback<Unit> {

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Toast.makeText(applicationContext, "Brak połączenia z internetem", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<Unit>, response: Response<Unit>){
                        Toast.makeText(applicationContext, "Zapisano na kurs", Toast.LENGTH_LONG).show()
                        loadData()
                    }
                })
        }
        completeDialog.setNegativeButton(R.string.dialog_negative) { _: DialogInterface, _: Int ->

        }
        completeDialog.create().show()
    }

    private fun loadData() {
        RetrofitClient.instance.getAvailableCourses(userId)
            .enqueue(object: retrofit2.Callback<List<CourseResponse>> {
                override fun onFailure(call: Call<List<CourseResponse>>, t: Throwable) {
                    Toast.makeText(applicationContext, "Błąd w połączeniu", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<CourseResponse>>, response: Response<List<CourseResponse>>) {
                    availableCoursesList.clear()
                    availableCoursesList.addAll(response.body()!!)
                    if(!myCoursesSelected) {
                        loadAvailableCourses()
                    }
                }
            })

        RetrofitClient.instance.getMyCourses(userId)
            .enqueue(object: retrofit2.Callback<List<CourseResponse>> {
                override fun onFailure(call: Call<List<CourseResponse>>, t: Throwable) {
                    Toast.makeText(applicationContext, "Błąd w połączeniu", Toast.LENGTH_LONG).show()
                    progressBarCourses.visibility = View.INVISIBLE
                }

                override fun onResponse(call: Call<List<CourseResponse>>, response: Response<List<CourseResponse>>) {
                    myCoursesList.clear()
                    myCoursesList.addAll(response.body()!!)
                    if(myCoursesSelected) {
                        loadMyCourses()
                    }
                    progressBarCourses.visibility = View.INVISIBLE
                }
            })
    }
}
