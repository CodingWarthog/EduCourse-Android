package com.wojcik.educourse.quizzes

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wojcik.educourse.R
import com.wojcik.educourse.quizzes.data.CourseResponse
import kotlinx.android.synthetic.main.item_course.view.*

class CoursesAdapter(val context: Context, private val courses: List<CourseResponse>) : RecyclerView.Adapter<CoursesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val course = courses[position]
        holder.setData(course, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(course: CourseResponse?, pos: Int) {
            itemView.courseName.text = course!!.name
            itemView.courseDescription.text = course.description
            itemView.courseOther.text = course.other
            itemView.setOnClickListener {
                if((context as CoursesActivity).getMyCoursesSelected()) {
                    (context).goToExams(pos)
                }
                else {
                    (context).joinCourse(course.id)
                }
            }
        }
    }
}