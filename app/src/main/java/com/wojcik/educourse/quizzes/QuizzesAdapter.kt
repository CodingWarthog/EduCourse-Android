package com.wojcik.educourse.quizzes

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wojcik.educourse.R
import com.wojcik.educourse.quizzes.data.Quiz
import kotlinx.android.synthetic.main.quiz_set_item.view.*

class QuizzesAdapter (val context: Context, private val quizzes: List<Quiz>) : RecyclerView.Adapter<QuizzesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quiz_set_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val course = quizzes[position]
        holder.setData(course, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(quiz: Quiz?, pos: Int) {
            itemView.name_txt_quiz.text = quiz!!.name
            itemView.lvl_txt_quiz.text = quiz.level
            itemView.setOnClickListener {
                (context as QuizzesActivity).goToQuizField(pos)
            }
        }
    }
}